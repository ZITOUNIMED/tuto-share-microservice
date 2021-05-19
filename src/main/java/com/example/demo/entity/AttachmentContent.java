package com.example.demo.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentContent extends AppElementContent{
    private String name;

    @Lob
    private byte[] data;

    private int width;
    private int height;
}
