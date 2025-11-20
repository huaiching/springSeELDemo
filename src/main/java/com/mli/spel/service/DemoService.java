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
public class DemoService {
    private static final Logger log = LoggerFactory.getLogger(DemoService.class);

    // SpEL 表達式解析器全局單例: 避免 多次 new 浪費記憶體
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    /**
     * 表達式緩存
     * - Key: ruleCode 字串
     * - Value: 已解析的 Expression 對象
     */
    private final Map<String, Expression> expressionCache = new ConcurrentHashMap<>();

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
        EvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        String ruleCode = "#age >= 30 and #income > 10000 and #address matches '.*台北市.*'";
//        String ruleCode = "#currency[0] != 'USD'";
//        String ruleCode = "#currency.contains(\"USD\")";
//        String ruleCode = "#address.substring(0,3) == \"台北市\"";
//        String ruleCode = "#user?.userDept matches '9025.*'";

        // 原始方法
//        String expression = ruleCode + " ? true : false";
//        Boolean result = parser.parseExpression(expression).getValue(context, Boolean.class);

        // AI 推薦方法
        Boolean result = Boolean.FALSE;
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

            result = expression.getValue(context, Boolean.class);
        } catch (ParseException e) {
            // 語法錯誤（規則寫錯）
            log.error("【Demo1 - SpEL 語法錯誤】檢核規則={} 錯誤訊息={}",
                    ruleCode, e.getMessage());
        } catch (EvaluationException e) {
            // 執行期錯誤（變數 null、類型不符等）
            log.error("【Demo1 - SpEL 執行失敗】檢核規則={} 錯誤訊息={}",
                    ruleCode, e.getMessage());
        } catch (Exception e) {
            // 其他未知錯誤
            log.error("【Demo1 - SpEL 未知異常】檢核規則={}",
                    ruleCode, e);
        }

        // 設定輸出
        ResultVo output = new ResultVo();
        output.setRule(ruleCode);
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
        EvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        String ruleCode = "#user?.userCode";

        // 原始方法
//        String result = PARSER.parseExpression(ruleCode).getValue(context, String.class);

        // AI 推薦方法
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
            log.error("【Demo2 - SpEL 語法錯誤】檢核規則={} 錯誤訊息={}",
                    ruleCode, e.getMessage());
        } catch (EvaluationException e) {
            // 執行期錯誤（變數 null、類型不符等）
            log.error("【Demo2 - SpEL 執行失敗】檢核規則={} 錯誤訊息={}",
                    ruleCode, e.getMessage());
        } catch (Exception e) {
            // 其他未知錯誤
            log.error("【Demo2 - SpEL 未知異常】檢核規則={}",
                    ruleCode, e);
        }

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
        EvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        String ruleCode = "T(java.lang.Math).max(#V001, T(java.lang.Math).max(#V002, (#V003 + #V004)))";

        // 原始方法
//        Double result = PARSER.parseExpression(ruleCode).getValue(context, Double.class);

        // AI 推薦方法
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
            log.error("【Demo3 - SpEL 語法錯誤】檢核規則={} 錯誤訊息={}",
                    ruleCode, e.getMessage());
        } catch (EvaluationException e) {
            // 執行期錯誤（變數 null、類型不符等）
            log.error("【Demo3 - SpEL 執行失敗】檢核規則={} 錯誤訊息={}",
                    ruleCode, e.getMessage());
        } catch (Exception e) {
            // 其他未知錯誤
            log.error("【Demo3 - SpEL 未知異常】檢核規則={}",
                    ruleCode, e);
        }

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
        StandardEvaluationContext context = new StandardEvaluationContext();
        dataMap.forEach(context::setVariable);

        try {
            // 將自製方法註冊進 SpEL 環境
            Method calcMethod = CalcService.class.getDeclaredMethod("calcTotalAmount", Double.class, Double.class, Double.class);
            context.registerFunction("calcTotalAmount", calcMethod);

            // SpEL 表達式：呼叫自製方法
            String ruleCode = "#calcTotalAmount(#baseSalary, #bonus, #taxRate)";

            // 原始方法
//            Double result = PARSER.parseExpression(ruleCode).getValue(context, Double.class);
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
                log.error("【Demo4 - SpEL 語法錯誤】檢核規則={} 錯誤訊息={}",
                        ruleCode, e.getMessage());
            } catch (EvaluationException e) {
                // 執行期錯誤（變數 null、類型不符等）
                log.error("【Demo4 - SpEL 執行失敗】檢核規則={} 錯誤訊息={}",
                        ruleCode, e.getMessage());
            } catch (Exception e) {
                // 其他未知錯誤
                log.error("【Demo4 - SpEL 未知異常】檢核規則={}",
                        ruleCode, e);
            }

            return result;

        } catch (Exception e) {
            return null;
        }
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
