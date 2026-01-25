package com.example.BankingSystem.Service;
import com.example.BankingSystem.DTO.AccountDTO;
import com.example.BankingSystem.DTO.TransferDTO;
import com.example.BankingSystem.Entity.*;
import com.example.BankingSystem.Helper.AccountHelper;
import com.example.BankingSystem.Repository.AccountRepos;
import com.example.BankingSystem.Repository.TransactionRepos;
import com.example.BankingSystem.Util.RandomUnits;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepos accountRepos;
    private final AccountHelper accountHelper;
    private final TransactionRepos transactionRepos;

    public Account createAccount(AccountDTO accountDTO, User user) throws Exception {
        Long accountNumber;
        validateAccountNonExistsForUser(accountDTO.getCode(), user.getUid());
        do{
            accountNumber = new RandomUnits().generateRandom(10);
        }while (accountRepos.existsByAccountNumber(accountNumber));
            var account = Account.builder()
                    .accountNumber(accountNumber)
                    .accountName(user.getFirstname() + " " + user.getLastname())
                    .balance(accountDTO.getBalance())
                    .owner(user)
                    .code(accountDTO.getCode())
                    .symbol(accountDTO.getSymbol())
                    .label(accountHelper.getCURRENCIES().get(accountDTO.getCode()))
                    .build();
            return accountRepos.save(account);
    }
    public List<Account> getUserAccounts(String uid){
        return accountRepos.findAllByOwnerUid(uid);
    }


    //Prevent duplicate account creation.
    public void validateAccountNonExistsForUser(String code, String uid) throws RuntimeException {
        if(accountRepos.existsByCodeAndOwnerUid(code,uid)){
            throw new RuntimeException("Account of validation is already");
        }
    }

    //Prevent users from accessing accounts that do not belong to them.
    public void validateAccountOwner(Account account, User user) throws UnsupportedOperationException{
        if(!account.getOwner().getUid().equals(user.getUid())){
            throw new UnsupportedOperationException("Invalid account owner");
        }
    }

    //check remaining funds in account
    public void validateSufficientFunds(Account account, double amount) throws UnsupportedOperationException{
        if(account.getBalance() < amount){
            throw new UnsupportedOperationException("Insufficient funds in this account");
        }
    }

    public Transactions performTransfer(Account senderAccount, Account receiverAccount, double amount) {
        validateSufficientFunds(senderAccount,amount * 1.01);
        senderAccount.setBalance(senderAccount.getBalance() - amount * 1.01);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
        accountRepos.saveAll(List.of(senderAccount,receiverAccount));
        var senderTransaction = Transactions.builder()
                .account(senderAccount)
                .status(Status.COMPLETED)
                .type(Type.WITHDRAW)
                .txFee(amount * 0.01)
                .amount(amount)
                .owner(senderAccount.getOwner())
                .build();
        var recipientTransaction = Transactions.builder()
                .account(receiverAccount)
                .status(Status.COMPLETED)
                .type(Type.DEPOSIT)
                .amount(amount)
                .owner(receiverAccount.getOwner())
                .build();
        //saveAll can be saved many objects so them need "List.of". It different with save normal
         transactionRepos.saveAll(List.of(senderTransaction,recipientTransaction));
         return senderTransaction;
    }

    public Transactions transferBalance(TransferDTO transferDTO, User user) {
        var senderAccount = accountRepos.findByCodeAndOwnerUid(transferDTO.getCode(),user.getUid())
                .orElseThrow(()-> new UnsupportedOperationException("Account of tupe currency do not exist for user"));
        var receiverAccount  = accountRepos.findByAccountNumber(transferDTO.getRecipientAccountNumber())
                .orElseThrow(() -> new UnsupportedOperationException("Recipient account not found"));
        return performTransfer(senderAccount,receiverAccount ,transferDTO.getAmount());
    }




}
