package com.example.demo.service.impl;

import com.example.demo.entity.Attachment;
import com.example.demo.repository.AttachmentRepository;
import com.example.demo.service.AttachmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachmentServiceImpl implements AttachmentService {
	private final AttachmentRepository attachmentRepository;

	public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
		this.attachmentRepository = attachmentRepository;
	}

	@Override
	public void save(Attachment t) {
		attachmentRepository.save(t);
	}

	@Override
	public Attachment findById(Long id) {
		return attachmentRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteById(Long id) {
		attachmentRepository.deleteById(id);
	}

	@Override
	public List<Attachment> findAll() {
		return attachmentRepository.findAll();
	}

	@Override
	public Page<Attachment> findAll(Pageable pageableRequest) {
		return null; // TODO
	}

	@Override
	public void saveAll(List<Attachment> list) {
		attachmentRepository.saveAll(list);
	}

	@Override
	public void delete(Attachment attachment) {
		// TODO
	}
}
