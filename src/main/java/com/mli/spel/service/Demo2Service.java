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
public class Demo2Service {
    private static final Logger log = LoggerFactory.getLogger(Demo2Service.class);

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
     * DTO資料範例: 取出指定欄位
     */
    public String spelDemo() {
        // 設定變數
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("user",new UserDto("A123456789","測試人員","90251"));

        // SpEL 變數 載入
        EvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        // 設定規則
        String ruleCode = "#user?.userCode";

        // SpEL 方法解析
        String result = "";
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

            result = expression.getValue(context, String.class);
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
