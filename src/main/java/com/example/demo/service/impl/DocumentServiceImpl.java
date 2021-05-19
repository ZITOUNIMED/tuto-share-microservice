package com.example.demo.service.impl;

import com.example.demo.entity.Document;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.service.DocumentService;
import com.example.demo.util.AppPermissionTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        super();
        this.documentRepository = documentRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public void save(Document document) {
        if (document != null) {
            LocalDate now = LocalDate.now();
            if (document.getCreationDate() == null) {
                document.setCreationDate(now);
            }
            document.setLastUpdateDate(now);
        }
        documentRepository.save(document);
    }

    @Override
    public Document findById(Long id) {
        return documentRepository.getOne(id);
    }

    @Override
    public void deleteById(Long id) {
        documentRepository.deleteById(id);
    }

    @Override
    public void saveAll(List<Document> list) {
        documentRepository.saveAll(list);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public Page<Document> findAll(Pageable pageableRequest) {
        return documentRepository.findAll(pageableRequest);
    }

    //	@PostFilter("hasPermission(filterObject, '"+AppPermissionTypes.PUBLIC+"')")
    @Override
    public Page<Document> findPublicDocuments(Pageable pageableRequest) {
        return documentRepository.findAll(pageableRequest);
    }

    @PostFilter("hasPermission(filterObject, '" + AppPermissionTypes.PUBLIC + "')")
    @Override
    public List<Document> findPublicDocuments() {
        return documentRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
//	@PostFilter("hasPermission(filterObject, '"+AppPermissionTypes.OWNER+"')")
    @Override
    public Page<Document> findMyDocuments(Pageable pageableRequest) {
        return documentRepository.findAll(pageableRequest);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, '"+AppPermissionTypes.OWNER+"')")
    @Override
    public List<Document> findMyDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public void delete(Document document) {
        // TODO
    }
}
