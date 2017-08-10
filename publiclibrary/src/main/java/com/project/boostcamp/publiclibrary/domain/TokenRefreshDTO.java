package com.project.boostcamp.publiclibrary.domain;

/**
 * Created by Hong Tae Joon on 2017-08-10.
 */

public class TokenRefreshDTO {
    private int type;
    private String token;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
