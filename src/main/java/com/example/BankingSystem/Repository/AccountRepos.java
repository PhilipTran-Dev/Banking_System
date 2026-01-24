package com.example.BankingSystem.Repository;

import com.example.BankingSystem.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepos extends JpaRepository<Account,String> {
    boolean existsByAccountNumber (Long accountNumber);

    boolean existsByCodeAndOwnerUid(String code, String uid);

    List<Account> findAllByOwnerUid(String uid);
}
