package com.example.demo.service.impl;

import com.example.demo.entity.Attachment;
import com.example.demo.entity.Document;
import com.example.demo.entity.Element;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.service.AttachmentService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@Profile("pre-prod", "prod")
@Primary
public class DocumentServiceImplForProd extends DocumentServiceImpl {
	private final AttachmentService attachmentService;

	public DocumentServiceImplForProd(DocumentRepository documentRepository, AttachmentService attachmentService) {
		super(documentRepository);
		this.attachmentService = attachmentService;
	}

	@Override
	 public Document findById(Long id){
		 Document document = super.findById(id);
		 document.getElements()
		 .stream()
		 .forEach(elt -> {
			 if(elt.getAttachmentId() != null && attachmentService.findById(elt.getAttachmentId()) != null){
				 Attachment attachment = attachmentService.findById(elt.getAttachmentId());
				 elt.setAttachment(attachment);
			 }
		 });
		 return document;
	 }
	
	@Override
	public void save(Document document){
		deleteUnAttachedAttachment(document);
		super.save(document);
		document.getElements()
		 .stream()
		 .forEach(elt -> {
			 if(elt.getAttachment() != null){
				 attachmentService.save(elt.getAttachment());
			 }
		 });
	}
	
	@Override
	public void deleteById(Long id){
		Document document = findById(id);
		/*
		document.getElements()
		.stream()
		.forEach(elt -> {
			if(elt.getAttachmentId()!=null && attachmentService.findById(elt.getAttachmentId()) != null){
				attachmentService.deleteById(elt.getAttachmentId());
			}
		});*/
		super.deleteById(id);
	}
	
	private void deleteUnAttachedAttachment(Document document){
		if(document.getId() != null){
			Document beforeUpdating = findById(document.getId());
			beforeUpdating.getElements()
			.stream()
			.filter(elt -> elt.getId() != null && !stillExisting(elt.getId(), document.getElements()))
			.forEach(elt -> {
				if(elt.getAttachmentId()!=null && attachmentService.findById(elt.getAttachmentId()) != null){
					attachmentService.deleteById(elt.getAttachmentId());
				}
			});
		}
	}

	private boolean stillExisting(Long id, List<Element> elements) {
		return elements.stream()
				.anyMatch(elt -> id.equals(elt.getId()));
	}
}
