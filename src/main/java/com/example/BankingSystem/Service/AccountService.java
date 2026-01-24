package com.example.BankingSystem.Service;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.example.BankingSystem.DTO.AccountDTO;
import com.example.BankingSystem.Entity.Account;
import com.example.BankingSystem.Entity.User;
import com.example.BankingSystem.Helper.AccountHelper;
import com.example.BankingSystem.Repository.AccountRepos;
import com.example.BankingSystem.Util.RandomUnits;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepos accountRepos;
    private final AccountHelper accountHelper;

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

    public void validateAccountNonExistsForUser(String code, String uid) throws RuntimeException {
        if(accountRepos.existsByCodeAndOwnerUid(code,uid)){
            throw new RuntimeException("Account of validation is already");
        }
    }

    public List<Account> getUserAccounts(String uid){
        return accountRepos.findAllByOwnerUid(uid);
    }
}
