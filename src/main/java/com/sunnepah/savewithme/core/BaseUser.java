package com.sunnepah.savewithme.core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sunnepah on 22/11/2016.
 * Sunday Ayandokun @sundayayandokun
 */
public class BaseUser extends BaseDocument {

    @JsonProperty("name")
    public String name;

    @JsonProperty("email")
    public String email;

    @JsonProperty("gender")
    public String gender;

    public String provider;

    private String token;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
