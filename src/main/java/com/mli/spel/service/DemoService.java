package com.mli.spel.service;

import com.mli.spel.dto.UserDto;
import com.mli.spel.vo.ResultVo;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class DemoService {
    /**
     * 規則範例
     */
    public ResultVo spelDemo1() {
        // 設定 變數資料
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("age", 40);
        dataMap.put("income", 50000);
        dataMap.put("address", "台北市內湖區石潭路58號1樓");
        dataMap.put("currency", Arrays.asList("TWD","USD"));
        dataMap.put("user",new UserDto("A123456789","測試人員","90251"));
        // 規則檢核
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        String rule = "#age >= 30 and #income > 10000 and #address matches '.*台北市.*'";
//        String rule = "#currency[0] != 'USD'";
//        String rule = "#currency.contains(\"USD\")";
//        String rule = "#address.substring(0,3) == \"台北市\"";
//        String rule = "#user?.userDept matches '9025.*'";

        String expression = rule + " ? true : false";
        Boolean result = parser.parseExpression(expression).getValue(context, Boolean.class);

        // 設定輸出
        ResultVo output = new ResultVo();
        output.setRule(expression);
        output.setResult(result);

        return output;
    }

    /**
     * DTO資料範例: 取出指定欄位
     */
    public String spelDemo2() {
        // 設定變數
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user",new UserDto("A123456789","測試人員","90251"));
        // 進行計算
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        String expression = "#user?.userCode";

        String result = parser.parseExpression(expression).getValue(context, String.class);

        return result;
    }

    /**
     * 金額計算範例: 三者取其大
     */
    public Double spelDemo3() {
        // 設定變數
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("V001", 500);
        dataMap.put("V002", 1000);
        dataMap.put("V003", 700);
        dataMap.put("V004", 450);
        // 進行計算
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        String expression = "T(java.lang.Math).max(#V001, T(java.lang.Math).max(#V002, (#V003 + #V004)))";

        Double result = parser.parseExpression(expression).getValue(context, Double.class);

        return result;
    }
    /**
     * 自製 Method 範例: 金額計算
     */
    public Double spelDemo4() {
        // 模擬輸入變數
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("baseSalary", 50000);  // 基本薪資
        dataMap.put("bonus", 8000);        // 獎金
        dataMap.put("taxRate", 0.9);       // 稅率

        // 初始化 SpEL 解析環境
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        try {
            // 將自製方法註冊進 SpEL 環境
            Method calcMethod = DemoService.class.getDeclaredMethod("calcTotalAmount", Double.class, Double.class, Double.class);
            context.registerFunction("calcTotalAmount", calcMethod);

            // SpEL 表達式：呼叫自製方法
            String rule = "#calcTotalAmount(#baseSalary, #bonus, #taxRate)";

            Double result = parser.parseExpression(rule).getValue(context, Double.class);
            return result;

        } catch (Exception e) {
            return null;
        }
    }

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
