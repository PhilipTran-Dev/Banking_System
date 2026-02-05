package com.example.BankingSystem.Controller;

import com.example.BankingSystem.Entity.Card;
import com.example.BankingSystem.Entity.Transactions;
import com.example.BankingSystem.Entity.User;
import com.example.BankingSystem.Service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;


    @GetMapping()
    public ResponseEntity<Card> getCard(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.getCard(user));
    }

    @PostMapping("/create")
    public ResponseEntity<Card> createCard (@RequestParam double amount, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.createCard(amount,user));
    }

    @PostMapping("/credit")
    public ResponseEntity<Transactions> creditCard(@RequestParam double amount, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.creditCard(amount, user));
    }

    @PostMapping("/debit")
    public ResponseEntity<Transactions> debitCard(@RequestParam double amount, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.debitCard(amount, user));
    }
}
