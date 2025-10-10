package com.mli.spel.config.dto.spel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "SpEL 驗證 輸入檔")
public class SpELInputDto {
    private List<DataMapInputDto> dataMapList;
    private String expression;

    public List<DataMapInputDto> getDataMapList() {
        return dataMapList;
    }

    public void setDataMapList(List<DataMapInputDto> dataMapList) {
        this.dataMapList = dataMapList;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
