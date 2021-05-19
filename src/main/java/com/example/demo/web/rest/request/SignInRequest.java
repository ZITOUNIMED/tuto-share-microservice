package com.example.demo.web.rest.request;

import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;
}
