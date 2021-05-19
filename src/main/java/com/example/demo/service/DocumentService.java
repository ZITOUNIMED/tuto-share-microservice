package com.example.demo.service;

import com.example.demo.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocumentService extends CrudService<Document, Long>{
	Page<Document> findMyDocuments(Pageable pageableRequest);

	List<Document> findMyDocuments();

	Page<Document> findPublicDocuments(Pageable pageableRequest);

	List<Document> findPublicDocuments();
}
