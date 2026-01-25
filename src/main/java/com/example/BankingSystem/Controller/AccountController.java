package com.example.BankingSystem.Controller;

import com.example.BankingSystem.DTO.AccountDTO;
import com.example.BankingSystem.DTO.TransferDTO;
import com.example.BankingSystem.Entity.Account;
import com.example.BankingSystem.Entity.Transactions;
import com.example.BankingSystem.Entity.User;
import com.example.BankingSystem.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;


    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDTO accountDTO, Authentication authentication) throws Exception {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.createAccount(accountDTO,user));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getUserAccount (Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getUserAccounts(user.getUid()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transactions> transferBalance(@RequestBody TransferDTO transferDTO, Authentication authentication) throws Exception{
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.transferBalance(transferDTO,user));
    }
}
