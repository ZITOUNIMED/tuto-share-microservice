package com.example.demo.util;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AppList implements Serializable {
    private String title;
    private List<String> items;
}
