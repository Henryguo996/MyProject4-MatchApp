package com.guohenry.matchapp.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "登入請求資料")
public class LoginRequest {

    @Schema(description = "會員 Email", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "會員密碼", example = "123456", required = true)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
