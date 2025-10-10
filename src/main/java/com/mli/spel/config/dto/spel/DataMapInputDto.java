package com.mli.spel.config.dto.spel;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "變數檔 (輸入用)")
public class DataMapInputDto {
    private String key;
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
