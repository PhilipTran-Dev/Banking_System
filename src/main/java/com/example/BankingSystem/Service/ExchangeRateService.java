package com.example.BankingSystem.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Getter
public class ExchangeRateService {

    private final RestTemplate restTemplate;
    private Map<String,Double> rates = new HashMap<>();
    private final Set<String> CURRENCIES = Set.of(
            "VND",
            "USD",
            "EUR",
            "JPY",
            "GBP",
            "CNY",
            "KRW",
            "SGD",
            "AUD",
            "CAD",
            "NZD"
    );

    @Value("${currencyApiKey}")
    private String apiKey;

    public void getExchangeRate(){
        String CURRENCY_API =  "https://api.currencyapi.com/v3/latest?apikey=";
        var response = restTemplate.getForEntity(CURRENCY_API + apiKey, JsonNode.class);
        var data = response.getBody().get("data");
        for (var currency : CURRENCIES){
            rates.put(currency, data.get(currency).get("value").doubleValue());
        }
        System.out.println(rates);
    }




}
