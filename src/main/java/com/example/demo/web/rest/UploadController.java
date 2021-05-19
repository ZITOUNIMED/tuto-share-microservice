package com.example.demo.web.rest;

import com.example.demo.entity.AttachmentContent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/upload")
public class UploadController {
	@PostMapping
	public ResponseEntity<AttachmentContent> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("width") int width,
			@RequestParam("height") int height){
		AttachmentContent attachment = null;
		try {
			attachment = AttachmentContent.builder()
					.data(file.getBytes())
					.width(width)
					.height(height)
					.name(file.getOriginalFilename())
			.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(attachment);
	}
}
