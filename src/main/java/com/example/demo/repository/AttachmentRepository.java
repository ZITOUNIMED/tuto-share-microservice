package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long>{

}
