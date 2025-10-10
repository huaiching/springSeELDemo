package com.mli.spel.config.service;

import com.mli.spel.config.vo.ResultVo;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

@Service
public class ValidateService {
    /**
     * spEL 表達式 語法驗證
     * @param expression 要驗證的 spEL 表達式
     * @return T.語法正確 / F.語法錯誤
     */
    public ResultVo validateExpression(String expression) {
        ResultVo resultVo = new ResultVo();
        ExpressionParser parser = new SpelExpressionParser();
        try {
            parser.parseExpression(expression);
            resultVo.setResult(Boolean.TRUE);
        } catch (ParseException e) {
            // SpEL 語法錯誤
            resultVo.setResult(Boolean.FALSE);
        }
        return resultVo;
    }
}
