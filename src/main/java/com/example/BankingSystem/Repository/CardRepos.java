package com.example.BankingSystem.Repository;

import com.example.BankingSystem.Entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepos extends JpaRepository<Card, String> {
}
