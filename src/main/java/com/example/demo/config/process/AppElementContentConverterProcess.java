package com.example.demo.config.process;

import com.example.demo.config.initData.UsersInitDataRunner;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.AttachmentContent;
import com.example.demo.entity.TextContent;
import com.example.demo.repository.AttachmentRepository;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.ElementRepository;
import com.example.demo.util.ElementTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(UsersInitDataRunner.class)
@Order(5)
public class AppElementContentConverterProcess implements ApplicationRunner {
	private final static Logger logger = LoggerFactory.getLogger(AppElementContentConverterProcess.class);
	private final DocumentRepository documentRepository;
	private final ElementRepository elementRepository;
	private final AttachmentRepository attachmentRepository;

	public AppElementContentConverterProcess(DocumentRepository documentRepository, ElementRepository elementRepository, AttachmentRepository attachmentRepository) {
		super();
		this.documentRepository = documentRepository;
		this.elementRepository = elementRepository;
		this.attachmentRepository = attachmentRepository;
	}

	@Override
	public void run(ApplicationArguments args) {
		logger.info("App Element Content Converter Process...");
		documentRepository.findAll()
				.parallelStream()
				.flatMap(doc -> doc.getElements().stream())
				.filter(doc -> doc.getAppElementContent() == null)
				.forEach(elt -> {
					ElementTypeEnum type = ElementTypeEnum.valueOf(elt.getType());
					switch (type){
						case TEXT:
						case BIG_TITLE:
						case MEDIUM_TITLE:
						case SMALL_TITLE:
						case VERY_SMALL_TITLE:
						case SOURCE_CODE:
						case HYPERLINK:
							TextContent textContent = TextContent.builder()
									.text(elt.getText())
									.build();
							textContent.setType(elt.getType());
							textContent.setText(elt.getText());
							elt.setAppElementContent(textContent);
							elt.setText(null);
							logger.info("New text is converted to TextContent...");
							break;
						case ATTACHMENT:
							if(elt.getAttachmentId() != null){
								Attachment attachment = attachmentRepository.findById(elt.getAttachmentId()).orElse(null);
								if(attachment != null){
									AttachmentContent attachmentContent = AttachmentContent.builder()
											.data(attachment.getData())
											.height(attachment.getHeight())
											.width(attachment.getWidth())
											.name(attachment.getName())
											.build();
									attachmentContent.setType(elt.getType());
									elt.setAppElementContent(attachmentContent);
									elt.setAttachmentId(null);
									logger.info("New Attachment is converted to TextContent...");
								}
							}
					}
					elt.getAppElementContent().setType(type.name());
					elementRepository.save(elt);
				});

	}
}