package com.example.BankingSystem.Service;

import com.example.BankingSystem.Entity.*;
import com.example.BankingSystem.Repository.AccountRepos;
import com.example.BankingSystem.Repository.CardRepos;
import com.example.BankingSystem.Repository.TransactionRepos;
import com.example.BankingSystem.Util.RandomUnits;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class CardService {
    private final CardRepos cardRepos;
    private final AccountService accountService;
    private final AccountRepos accountRepos;
    private final TransactionRepos transactionRepos;

    public Card getCard(User user) {
        return cardRepos.findByOwnerUid(user.getUid())
                .orElseThrow(()-> new RuntimeException("Cannot found user id"));
    }

    public Card createCard(double amount, User user) {
        if(amount < 2 ){
            throw new IllegalArgumentException("Amount should be at least 2$");
        }
        if(!accountRepos.existsByCodeAndOwnerUid("USD",user.getUid())){
            throw new IllegalArgumentException("USD account not found for this user card, card cannot be created");
        }
        var usdAccount = accountRepos.findByCodeAndOwnerUid("USD", user.getUid())
                .orElseThrow(()-> new RuntimeException("Cannot found USD acount"));
        accountService.validateSufficientFunds(usdAccount, amount);
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        long cardNumber;
        do{
            cardNumber = generateCardNumber();
        }while (cardRepos.existsByCardNumber(cardNumber));

        Card card = Card.builder()
                .cardHolder(user.getFirstname()+ " " + user.getLastname())
                .exp(LocalDateTime.now().plusYears(3))
                .cvv(new RandomUnits().generateRandom(3).toString())
                .build();
        cardRepos.save(card);
        accountService.createAccountTransaction(1, Type.WITHDRAW, 0.00, user, usdAccount);
        accountService.createAccountTransaction(amount-1, Type.WITHDRAW, 0.00, user, usdAccount);
        createCardTransaction(amount,Type.WITHDRAW,0.00, user, card);
        accountRepos.save(usdAccount);
        return card;
    }

    public Long generateCardNumber(){
        return new RandomUnits().generateRandom(16);
    }


    public Transactions creditCard(double amount, User user) {
        var usdAccount = accountRepos.findByCodeAndOwnerUid("USD", user.getUid())
                .orElseThrow(()-> new RuntimeException("Cannot found USD acount"));
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        accountService.createAccountTransaction(amount, Type.WITHDRAW, 0.00, user,usdAccount);
        var card = user.getCard();
        card.setBalance(card.getBalance() + amount);
        return createCardTransaction(amount, Type.CREDIT, 0.00, user, card);
    }

    public Transactions debitCard(double amount, User user) {
        var usdAccount = accountRepos.findByCodeAndOwnerUid("USD", user.getUid())
                .orElseThrow(()-> new RuntimeException("Cannot found USD acount"));
        usdAccount.setBalance(usdAccount.getBalance() + amount);
        accountService.createAccountTransaction(amount-1, Type.WITHDRAW, 0.00, user,usdAccount);
        var card = user.getCard();
        card.setBalance(card.getBalance() - amount);
        return createCardTransaction(amount, Type.DEBIT, 0.00, user, card);
    }






    public Transactions createCardTransaction(double amount, Type type, double txFee, User user, Card card) {
        var cardTransaction = Transactions.builder()
                .txFee(txFee)
                .amount(amount)
                .type(type)
                .status(Status.COMPLETED)
                .owner(user)
                .card(card)
                .build();
        return transactionRepos.save(cardTransaction);
    }
}
