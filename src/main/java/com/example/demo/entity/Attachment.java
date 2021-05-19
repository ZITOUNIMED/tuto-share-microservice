package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ATTACHMENT_ID")
	private Long id;
	
	private String name;
	
	@Lob
	private byte[] data;
	
	private int width;
	private int height;
}
