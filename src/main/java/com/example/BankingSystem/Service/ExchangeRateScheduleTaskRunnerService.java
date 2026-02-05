package com.example.BankingSystem.Service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ExchangeRateScheduleTaskRunnerService implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(ExchangeRateScheduleTaskRunnerService.class);
    private final ScheduledExecutorService scheduledExecutorService;
    private final ExchangeRateService exchangeRateService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Calling the currency api endpoints for exchange rate");
        scheduledExecutorService.scheduleWithFixedDelay(()->
                exchangeRateService.getExchangeRate(),
                0,
                24,
                TimeUnit.HOURS
        );
        logger.info("Ended calling");
    }
}
