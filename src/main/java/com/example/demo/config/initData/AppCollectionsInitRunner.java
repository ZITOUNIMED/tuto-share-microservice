package com.example.demo.config.initData;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.demo.entity.AppCollection;
import com.example.demo.entity.Document;
import com.example.demo.entity.User;
import com.example.demo.repository.AppCollectionRepository;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.UserRepository;

@Component
@ConditionalOnProperty(value="init.app-collections.data")
@ConditionalOnBean({UsersInitDataRunner.class, DocumentsInitDataRunner.class})
@Order(4)
public class AppCollectionsInitRunner implements ApplicationRunner{
	private final static Logger logger = LoggerFactory.getLogger(AppCollectionsInitRunner.class);

	private final AppCollectionRepository appCollectionRepository;
	private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
	
	public AppCollectionsInitRunner(AppCollectionRepository appCollectionRepository,
			DocumentRepository documentRepository, UserRepository userRepository) {
		super();
		this.appCollectionRepository = appCollectionRepository;
		this.documentRepository = documentRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("init appCollections data...");// TODO: replace with logger
		
		AppCollection appCollection = new AppCollection("sourcer1", "Test collection", "some description here!");
		
		User user1 = userRepository.findByUsername("user1");
		appCollection.setMembers(new HashSet<>(Arrays.asList(user1)));
		
		Set<Document> documents = documentRepository.findDocumentsByOwnerUsername("sourcer1");
		appCollection.setDocuments(documents);
		
		appCollectionRepository.save(appCollection);
		
		logger.info("New collection 'Test collection' is added.");
		logger.info("New memember 'user1' is added to collection 'Test collection'.");
		
		logger.info("Documents are added to collection 'Test collection': " +
				documents.stream().map(doc -> doc.getName()).collect(Collectors.toList()));
	}

}
