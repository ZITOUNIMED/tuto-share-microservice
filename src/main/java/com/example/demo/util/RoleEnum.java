package com.example.demo.util;

public enum RoleEnum {
    ROLE_USER("ROLE_USER", "USER"),
    ROLE_ADMIN("ROLE_ADMIN", "ADMIN"),
    ROLE_GUEST("ROLE_GUEST", "GUEST"),
    ROLE_SOURCER("ROLE_SOURCER", "SOURCER");

    private String name;
    private String role;

    RoleEnum(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole(){
        return this.role;
    }
}
