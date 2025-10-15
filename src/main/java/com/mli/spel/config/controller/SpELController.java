package com.mli.spel.config.controller;

import com.mli.spel.config.dto.ValidateSpELSerachDto;
import com.mli.spel.config.service.DemoService;
import com.mli.spel.config.service.ValidateService;
import com.mli.spel.config.vo.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SpEL Controller", description = "SpEL Demo API 接口")
@RestController
public class SpELController {
    @Autowired
    private ValidateService validateService;
    @Autowired
    private DemoService demoService;

    @Operation(summary = "spEL 表達式 語法驗證", description = "spEL 表達式 語法驗證")
    @PostMapping("/verifySpelExpression")
    public ResponseEntity<String> validateSpEL(@RequestBody ValidateSpELSerachDto validateSpELSerachDto) {
        String result = validateService.verifySpelExpression(validateSpELSerachDto.getSpelExpr());
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "規則範例", description = "spEL 規則範例")
    @PostMapping("/demoExample")
    public ResponseEntity<ResultVo> demoExample() {
        ResultVo resultVo = demoService.demoExample();
        return ResponseEntity.ok(resultVo);
    }

    @Operation(summary = "金額計算範例: 三者取其大", description = "金額計算範例: 三者取其大")
    @PostMapping("/demoExample1")
    public ResponseEntity<Double> demoExample1() {
        Double resultVo = demoService.spelCalcDemo1();
        return ResponseEntity.ok(resultVo);
    }
}
