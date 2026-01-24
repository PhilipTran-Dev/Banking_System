package com.example.BankingSystem.Util;

public class RandomUnits {
    public Long generateRandom(int length){
        StringBuilder ramdom = new StringBuilder(length);
        for(int i = 0; i < length; i++){
            int digit = (int) (Math.random() * 10);
            ramdom.append(digit);
        }
        return Long.parseLong(ramdom.toString());
    }
}
