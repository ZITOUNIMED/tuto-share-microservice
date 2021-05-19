package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public class AppElementContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="APP_ELEMENT_CONTENT_ID")
    protected Long id;
    protected String type;
}
