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
    private final Map<String, String> CURRENCIES = Map.ofEntries(
            Map.entry("VND", "Vietnamese Dong"),
            Map.entry("USD", "US Dollar"),
            Map.entry("EUR", "Euro"),
            Map.entry("JPY", "Japanese Yen"),
            Map.entry("GBP", "British Pound"),
            Map.entry("CNY", "Chinese Yuan"),
            Map.entry("KRW", "Korean Won"),
            Map.entry("SGD", "Singapore Dollar"),
            Map.entry("AUD", "Australian Dollar"),
            Map.entry("CAD", "Canadian Dollar"),
            Map.entry("NZD", "New Zealand Dollar")
    );


}
