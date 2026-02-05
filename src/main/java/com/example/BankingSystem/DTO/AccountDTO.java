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
    private String symbol;
    private double balance;

}
