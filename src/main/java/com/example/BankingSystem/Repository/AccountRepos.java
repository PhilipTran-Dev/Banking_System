package com.example.BankingSystem.Repository;

import com.example.BankingSystem.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepos extends JpaRepository<Account,String> {
}
