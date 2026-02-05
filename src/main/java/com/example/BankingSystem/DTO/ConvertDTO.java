package com.example.BankingSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConvertDTO {
    private String fromCurrency;
    private String toCurrency;
    private double amount;
}
