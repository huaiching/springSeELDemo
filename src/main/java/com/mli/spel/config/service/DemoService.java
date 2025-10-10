package com.mli.spel.config.service;

import com.mli.spel.config.vo.ResultVo;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class DemoService {
    public ResultVo demoExample() {
        // 設定 變數資料
        Map<String, Object> dataMap = getDataMap();
        // 規則檢核
        Boolean ruleResult = spELRule(dataMap);

        ResultVo result = new ResultVo();
        result.setResult(ruleResult);

        return result;
    }

    /**
     * 設定變數資料
     * @return
     */
    private Map<String, Object> getDataMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("age", 40);
        result.put("income", 50000);
        result.put("address", "台北市內湖區石潭路58號1樓");
        result.put("currency", Arrays.asList("TWD","USD"));

        return result;
    }

    /**
     * spEL 規則檢核
     * @return
     */
    private Boolean spELRule(Map<String, Object> dataMap) {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        String rule = "#age >= 30 and #income > 10000 and #address matches '.*台北市.*'";
//        String rule = "#currency[0] != 'USD'";
//        String rule = "#currency.contains(\"USD\")";
//        String rule = "#address.substring(0,3) == \"台北市\"";
        String expression = rule + " ? true : false";
        Boolean result = parser.parseExpression(
                        expression)
                .getValue(context, Boolean.class);

        return result;
    }
}
