package com.example.BankingSystem.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private String code;
    private String label;
    private char symbol;
    private double balance;

}
