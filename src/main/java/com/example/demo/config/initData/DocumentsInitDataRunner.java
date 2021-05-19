package com.example.demo.config.initData;

import com.example.demo.entity.Attachment;
import com.example.demo.entity.Document;
import com.example.demo.entity.Element;
import com.example.demo.entity.User;
import com.example.demo.repository.AttachmentRepository;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.ConfidentialityEnum;
import com.example.demo.util.ElementTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@ConditionalOnProperty(value="init.documents.data")
@ConditionalOnBean(UsersInitDataRunner.class)
@Order(3)
public class DocumentsInitDataRunner implements ApplicationRunner {
	private final static Logger logger = LoggerFactory.getLogger(DocumentsInitDataRunner.class);
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

	@Value("classpath:static/images/sample.jpg")
	Resource imageFile;

	public DocumentsInitDataRunner(DocumentRepository documentRepository, UserRepository userRepository, AttachmentRepository attachmentRepository) {
		super();
		this.documentRepository = documentRepository;
		this.userRepository = userRepository;
		this.attachmentRepository = attachmentRepository;
	}

	@Override
    public void run(ApplicationArguments args){
        logger.info("init documents data...");

        User sourcer1 = userRepository.findByUsername("sourcer1");
        createUserDocuments(sourcer1, Arrays.asList("Mockito", "Java 8"), ConfidentialityEnum.PRIVATE);
        createUserDocuments(sourcer1, Arrays.asList("JUnit", "Maven"), ConfidentialityEnum.PUBLIC);
        
        User sourcer2 = userRepository.findByUsername("sourcer2");
        createUserDocuments(sourcer2, Arrays.asList("Rxjs", "Spring framework"), ConfidentialityEnum.PRIVATE);
    }
    
	private void createUserDocuments(User user, List<String> names, ConfidentialityEnum confidentiality) {

		if(user != null && names!=null) {
            LocalDate now = LocalDate.now();
    		names.stream()
            .map(name -> Document.builder()
                    .name(name)
                    .confidentiality(confidentiality.getName())
                    .ownerUsername(user.getUsername())
                    .author(getAuthor(user.getFirstname(), user.getLastname()))
                    .creationDate(now)
                    .lastUpdateDate(now)
                    .build())
            .peek(document -> {
                document.setElements(IntStream.range(0, 3)
                        .mapToObj(number -> document.getName() + " text " + number)
                        .map(text -> Element.builder()
                                .type("TEXT")
                                .text(text)
                                .page(0)
                                .row(Integer.parseInt(text.substring(text.length() - 1)))
                                .build() )
                        .collect(Collectors.toList()));
            })
            .collect(Collectors.toList())
            .forEach(document -> {
            	Element elt = createAttachmentElement(document.getElements().size(), 0);
            	if(elt != null){
					document.getElements().add(elt);
				}
                documentRepository.save(document);
               	logger.info("add new document "+ confidentiality.getName() +" with name: " + document.getName()+ " for user: " + user.getUsername());
            });
    	} else {
    		logger.warn("can't find user!");
    	}
    }

	private Element createAttachmentElement(int row, int page) {
		// upload image and save it in the database
		try {
			File f1 = imageFile.getFile();
			byte[] data = Files.readAllBytes(f1.toPath());
			Attachment attachment = Attachment.builder()
					.width(250)
					.height(250)
					.name("My Photo")
					.data(data)
					.build();

			attachmentRepository.save(attachment);

			return Element.builder()
					.attachmentId(attachment.getId())
					.page(page)
					.row(row)
					.type(ElementTypeEnum.ATTACHMENT.name())
					.build();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}


	private String getAuthor(String firstname, String lastname){
    	StringBuilder stb = new StringBuilder();
    	
    	if(firstname != null){
    		stb.append(firstname);
    	}
    	if(lastname != null){
    		stb.append(" ").append(lastname);
    	}
    	return stb.toString();
    }
}
