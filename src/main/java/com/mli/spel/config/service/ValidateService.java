package com.mli.spel.config.service;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

@Service
public class ValidateService {
    /**
     * spEL 表達式 語法驗證
     * @param expression 要驗證的 spEL 表達式
     * @return 驗證結果
     */
    public String verifySpelExpression(String expression) {
        ExpressionParser parser = new SpelExpressionParser();

        // 過濾不允許的賦值運算子
        if (expression.contains(" = ")) {
            return "不合格";
        }

        // 語法解析
        try {
            parser.parseExpression(expression);
            return "合格";
        } catch (ParseException e) {
            // SpEL 語法錯誤
            return "不合格";
        }
    }
}
