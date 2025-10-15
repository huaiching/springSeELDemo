package com.mli.spel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "使用者資訊")
public class UserDto {
    private String userCode;
    private String userName;
    private String userDept;

    public UserDto(String userCode, String userName, String userDept) {
        this.userCode = userCode;
        this.userName = userName;
        this.userDept = userDept;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDept() {
        return userDept;
    }

    public void setUserDept(String userDept) {
        this.userDept = userDept;
    }
}
