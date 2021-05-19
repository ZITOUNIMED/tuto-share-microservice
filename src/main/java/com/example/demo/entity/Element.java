package com.example.demo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.example.demo.deserializer.AppElementContentDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Element {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ELEMENT_ID")
	private Long id;
	private String type;
	
	@Column(length=1200)
	private String text;
	private int row;
	private int page;
	
	private Long attachmentId;
	
	@Transient
	private Attachment attachment;

	@OneToOne(cascade = CascadeType.ALL)
	@JsonDeserialize(using = AppElementContentDeserializer.class)
	private AppElementContent appElementContent;
	
	public void setAttachment(Attachment attachment){
		if(attachment != null){
			attachmentId = attachment.getId();
		}
		this.attachment = attachment;
	}
}
