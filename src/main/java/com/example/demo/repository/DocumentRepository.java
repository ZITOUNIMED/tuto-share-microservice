package com.example.demo.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>{
	Set<Document> findDocumentsByOwnerUsername(String ownerUsername);
}
