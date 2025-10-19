package com.mli.spel.service;

import org.springframework.stereotype.Service;

@Service
public class CalcService {


    /**
     * 自製金額計算方法
     */
    private static Double calcTotalAmount(Double base, Double bonus, Double taxRate) {
        if (base == null) base = 0.0;
        if (bonus == null) bonus = 0.0;
        if (taxRate == null) taxRate = 1.0;

        // 總金額 = (基本薪資 + 獎金) × 稅率
        return (base + bonus) * taxRate;
    }
}
