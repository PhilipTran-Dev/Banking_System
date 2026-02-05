package com.example.BankingSystem.Repository;

import com.example.BankingSystem.Entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepos extends JpaRepository<Card, String> {
    Boolean existsByCardNumber (Long cardNumber);
    Optional<Card> findByOwnerUid(String uid);
}
