package com.mli.spel.controller;

import com.mli.spel.dto.ValidateSpELSerachDto;
import com.mli.spel.service.*;
import com.mli.spel.vo.ResultVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SpEL Controller", description = "SpEL Demo API 接口")
@RequestMapping("/demo")
@RestController
public class SpELController {
    @Autowired
    private ValidateService validateService;
    @Autowired
    private SampleSpelService sampleSpelService;
    @Autowired
    private Demo1Service demo1Service;
    @Autowired
    private Demo2Service demo2Service;
    @Autowired
    private Demo3Service demo3Service;
    @Autowired
    private Demo4Service demo4Service;

    @Operation(summary = "spEL 表達式 語法驗證", description = "spEL 表達式 語法驗證")
    @PostMapping("/verifySpelExpression")
    public ResponseEntity<String> validateSpEL(@RequestBody ValidateSpELSerachDto validateSpELSerachDto) {
        String result = validateService.verifySpelExpression(validateSpELSerachDto.getSpelExpr());
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "規則範例 (簡易寫法)", description = "spEL 規則範例 (簡易寫法)")
    @PostMapping("/sampleDemp")
    public ResponseEntity<ResultVo> sampleDemp() {
        ResultVo resultVo = sampleSpelService.spelDemo();
        return ResponseEntity.ok(resultVo);
    }


    @Operation(summary = "規則範例", description = "spEL 規則範例")
    @PostMapping("/spelDemo1")
    public ResponseEntity<ResultVo> spelDemo1() {
        ResultVo resultVo = demo1Service.spelDemo();
        return ResponseEntity.ok(resultVo);
    }

    @Operation(summary = "DTO資料範例: 取出指定欄位", description = "DTO資料範例: 取出指定欄位")
    @PostMapping("/spelDemo2")
    public ResponseEntity<String> spelDemo2() {
        String resultVo = demo2Service.spelDemo();
        return ResponseEntity.ok(resultVo);
    }

    @Operation(summary = "金額計算範例: 三者取其大", description = "金額計算範例: 三者取其大")
    @PostMapping("/spelDemo3")
    public ResponseEntity<Double> spelDemo3() {
        Double resultVo = demo3Service.spelDemo();
        return ResponseEntity.ok(resultVo);
    }

    @Operation(summary = "自製 Method 範例: 金額計算", description = "自製 Method 範例: 金額計算")
    @PostMapping("/spelDemo4")
    public ResponseEntity<Double> spelDemo4() {
        Double resultVo = demo4Service.spelDemo();
        return ResponseEntity.ok(resultVo);
    }
}
