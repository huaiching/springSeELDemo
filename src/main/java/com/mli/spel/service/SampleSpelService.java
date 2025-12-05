package com.mli.spel.service;

import com.mli.spel.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SampleSpelService {
    private static final Logger log = LoggerFactory.getLogger(SampleSpelService.class);

    public ResultVo spelDemo() {
        // 設定 變數資料
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("age", 40);
        dataMap.put("income", 50000);
        dataMap.put("address", "台北市內湖區石潭路58號1樓");

        // 設定 SpEL 解析器
        ExpressionParser parser = new SpelExpressionParser();

        // SpEL 變數 載入
        EvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        String ruleCode = "#age >= 30 and #income > 10000 and #address matches '.*台北市.*'";

        // SpEL 方法解析
        Boolean result = Boolean.FALSE;
        try {
            String expression = ruleCode + " ? true : false";
            result = parser.parseExpression(expression).getValue(context, Boolean.class);
        } catch (ParseException e) {
            // 語法錯誤（規則寫錯）
            log.error("【SpEL 語法錯誤】檢核規則={} 錯誤訊息={}",
                    ruleCode, e.getMessage());
        } catch (EvaluationException e) {
            // 執行期錯誤（變數 null、類型不符等）
            log.error("【SpEL 執行失敗】檢核規則={} 錯誤訊息={}",
                    ruleCode, e.getMessage());
        } catch (Exception e) {
            // 其他未知錯誤
            log.error("【SpEL 未知異常】檢核規則={}",
                    ruleCode, e);
        }

        // 設定輸出
        ResultVo output = new ResultVo();
        output.setRule(ruleCode);
        output.setResult(result);

        return output;
    }
}
