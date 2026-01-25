package com.example.BankingSystem.Repository;

import com.example.BankingSystem.Entity.Account;
import com.example.BankingSystem.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepos extends JpaRepository<Account,String> {
    boolean existsByAccountNumber (Long accountNumber);

    boolean existsByCodeAndOwnerUid(String code, String uid);

    //return many records
    List<Account> findAllByOwnerUid(String uid);

    //return only one record (roughly find about information relative in unique)
    Optional<Account> findByCodeAndOwnerUid(String code,String uid);

    Optional<Account> findByAccountNumber(Long recipientAccountNumber);
}
