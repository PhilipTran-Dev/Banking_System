package com.example.BankingSystem.Repository;

import com.example.BankingSystem.Entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepos extends JpaRepository<Transactions,String> {
}
