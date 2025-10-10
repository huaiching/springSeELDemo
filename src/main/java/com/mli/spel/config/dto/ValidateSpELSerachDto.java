package com.mli.spel.config.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "檢核 spEL 語句")
public class ValidateSpELSerachDto {
    private String spelExpr;

    public String getSpelExpr() {
        return spelExpr;
    }

    public void setSpelExpr(String spelExpr) {
        this.spelExpr = spelExpr;
    }
}
