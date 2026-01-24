package com.example.BankingSystem.Helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
@Getter
@Setter
@RequiredArgsConstructor
public class AccountHelper {
    private final Map<String, String> CURRENCIES = Map.of(
            "VND", "Vietnamese Dong",
            "USD", "US Dollar",
            "EUR", "Euro",
            "JPY", "Japanese Yen",
            "GBP", "British Pound",
            "CNY", "Chinese Yuan",
            "KRW", "Korean Won",
            "SGD", "Singapore Dollar",
            "AUD", "Australian Dollar",
            "CAD", "Canadian Dollar"
    );

}
