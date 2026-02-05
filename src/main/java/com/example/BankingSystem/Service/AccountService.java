package com.example.BankingSystem.Service;
import com.example.BankingSystem.DTO.AccountDTO;
import com.example.BankingSystem.DTO.ConvertDTO;
import com.example.BankingSystem.DTO.TransferDTO;
import com.example.BankingSystem.Entity.*;
import com.example.BankingSystem.Helper.AccountHelper;
import com.example.BankingSystem.Repository.AccountRepos;
import com.example.BankingSystem.Repository.TransactionRepos;
import com.example.BankingSystem.Util.RandomUnits;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
    private final AccountRepos accountRepos;
    private final AccountHelper accountHelper;
    private final TransactionRepos transactionRepos;
    private final ExchangeRateService exchangeRateService;

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
                    .symbol(String.valueOf(accountDTO.getSymbol()))
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

    public Transactions performTransfer(Account senderAccount, Account receiverAccount, double amount, User user) {
        validateSufficientFunds(senderAccount,amount * 1.01);
        senderAccount.setBalance(senderAccount.getBalance() - amount * 1.01);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
        accountRepos.saveAll(List.of(senderAccount,receiverAccount));
        var senderTransaction = createAccountTransaction(amount, Type.WITHDRAW, amount * 0.01, user, senderAccount);
        var receiverTransaction = createAccountTransaction(amount, Type.DEPOSIT, amount * 0.00, user, receiverAccount);
        //saveAll can be saved many objects so them need "List.of". It different with save normal
         transactionRepos.saveAll(List.of(senderTransaction,receiverTransaction));
         return senderTransaction;
    }

    public Transactions transferBalance(TransferDTO transferDTO, User user) {
        var senderAccount = accountRepos.findByCodeAndOwnerUid(transferDTO.getCode(),user.getUid())
                .orElseThrow(()-> new UnsupportedOperationException("Account of type currency do not exist for user"));
        var receiverAccount  = accountRepos.findByAccountNumber(transferDTO.getRecipientAccountNumber())
                .orElseThrow(() -> new UnsupportedOperationException("Recipient account not found"));
        return performTransfer(senderAccount,receiverAccount ,transferDTO.getAmount(),user);
    }


    //it made from ExchangeRateService & ExchangeRateScheduleTaskRunnerService
    public Map<String,Double> getExchangeRate(){
        return exchangeRateService.getRates();
    }

    public void validateAmount(double amount) throws Exception{
        if(amount < 0 ){
            throw new Exception("Invalid Amount");
        }
    }

    public void validateDifferentCurrencyType(ConvertDTO convertDTO) throws Exception{
        if(convertDTO.getToCurrency().equals(convertDTO.getFromCurrency())){
            throw new Exception("Cannot exchange because same currency !");
        }
    }

    public void validateAccountOwner(ConvertDTO convertDTO, String uid) throws Exception{
        accountRepos.findByCodeAndOwnerUid(convertDTO.getFromCurrency(),uid)
                .orElseThrow(()-> new RuntimeException("Invalid account to convert"));
        accountRepos.findByCodeAndOwnerUid(convertDTO.getToCurrency(),uid)
                .orElseThrow(()-> new RuntimeException("Invalid account to convert"));
    }

    public void validateConversion(ConvertDTO convertDTO, String uid) throws Exception{
        validateAmount(convertDTO.getAmount());
        validateAccountOwner(convertDTO,uid);
        validateDifferentCurrencyType(convertDTO);
        validateSufficientFunds(accountRepos.findByCodeAndOwnerUid(convertDTO.getFromCurrency(), uid).get(),convertDTO.getAmount());
    }

    public Transactions convertCurrency(ConvertDTO convertDTO, User user) throws Exception{
        validateConversion(convertDTO,user.getUid());
        var rates = exchangeRateService.getRates();
        var sendingRate = rates.get(convertDTO.getFromCurrency());
        var receiveRate = rates.get(convertDTO.getToCurrency());
        var computedAmount = receiveRate/sendingRate * convertDTO.getAmount();
        var fromAccount = accountRepos.findByCodeAndOwnerUid(convertDTO.getFromCurrency(),user.getUid()).get();
        var toAccount = accountRepos.findByCodeAndOwnerUid(convertDTO.getToCurrency(),user.getUid()).get();
        fromAccount.setBalance(fromAccount.getBalance() - convertDTO.getAmount() * 1.01);
        toAccount.setBalance(toAccount.getBalance() + computedAmount);
        accountRepos.saveAll(List.of(fromAccount,toAccount));
        var fromTransaction = createAccountTransaction(convertDTO.getAmount(), Type.CONVERSION, convertDTO.getAmount() * 0.01 , user, fromAccount );
        var toTransaction = createAccountTransaction(computedAmount, Type.DEPOSIT, convertDTO.getAmount() * 0.00 , user, toAccount );
        return fromTransaction;
    }

    //ínstead of transaction function manually
    public Transactions createAccountTransaction(double amount, Type type, double txFee, User user, Account account) {
        var accountTransaction = Transactions.builder()
                .amount(amount)
                .txFee(txFee)
                .owner(user)
                .account(account)
                .type(type)
                .status(Status.COMPLETED)
                .build();
        return transactionRepos.save(accountTransaction);
    }



}
