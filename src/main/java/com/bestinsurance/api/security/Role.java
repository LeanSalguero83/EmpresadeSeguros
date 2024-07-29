package com.bestinsurance.api.security;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    FRONT_OFFICE("ROLE_FRONT_OFFICE"),
    BACK_OFFICE("ROLE_BACK_OFFICE"),
    CUSTOMER("ROLE_CUSTOMER");

    private String value;
    Role(String val) {
        this.value = val;
    }
    public String value() {
        return this.value;
    }
}
