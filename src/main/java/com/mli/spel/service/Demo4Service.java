package com.mli.spel.service;

import com.mli.spel.dto.UserDto;
import com.mli.spel.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Demo4Service {
    private static final Logger log = LoggerFactory.getLogger(Demo4Service.class);

    /**
     * SpEL 表達式解析器全局單例: 避免 多次 new 浪費記憶體
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    /**
     * 表達式緩存
     * - Key: SpEL 檢核規則 字串
     * - Value: 已解析的 Expression 對象
     */
    private final Map<String, Expression> expressionCache = new ConcurrentHashMap<>();

    /**
     * 自製 Method 範例: 金額計算
     */
    public Double spelDemo() {
        // 設定 變數資料
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("baseSalary", 50000);  // 基本薪資
        dataMap.put("bonus", 8000);        // 獎金
        dataMap.put("taxRate", 0.9);       // 稅率

        // SpEL 變數 載入
        StandardEvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        try {
            // 將自製方法註冊進 SpEL 環境
            spelRegister(context);

            // 設定規則
            String ruleCode = "#calcTotalAmount(#baseSalary, #bonus, #taxRate)";

            // SpEL 方法解析
            Double result = 0.0;
            try {
                // 從緩存獲取或解析（Key 是完整的 ruleCode）
                Expression expression = expressionCache.computeIfAbsent(ruleCode, code -> {
                    // 檢查緩存大小，防止無限增長
                    if (expressionCache.size() >= 1000) {
                        log.warn("表達式緩存已達上限 {}，清除最舊的 20% 項目", 1000);
                        evictOldestEntries(expressionCache);
                    }
                    return PARSER.parseExpression(code);
                });

                result = expression.getValue(context, Double.class);
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

            return result;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 將自製方法註冊進 SpEL 環境
     * @param context
     * @throws NoSuchMethodException
     */
    private void spelRegister(StandardEvaluationContext context) throws NoSuchMethodException {
        Method calcMethod = CalcService.class.getDeclaredMethod("calcTotalAmount", Double.class, Double.class, Double.class);
        context.registerFunction("calcTotalAmount", calcMethod);
    }

    /**
     * 當緩存滿時，清除最舊的 20% 項目
     */
    private void evictOldestEntries(Map<String, Expression> expressionCache) {
        int removeCount = 1000 / 5; // 清除 20%
        Iterator<String> iterator = expressionCache.keySet().iterator();

        int removed = 0;
        while (iterator.hasNext() && removed < removeCount) {
            iterator.next();
            iterator.remove();
            removed++;
        }

        log.info("已清除 {} 個舊的表達式緩存項目", removed);
    }
}
