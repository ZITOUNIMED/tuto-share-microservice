package com.example.demo.util;

public enum ConfidentialityEnum {
    PRIVATE("PRIVATE"),
    PUBLIC("PUBLIC");

    private String name;

    ConfidentialityEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
