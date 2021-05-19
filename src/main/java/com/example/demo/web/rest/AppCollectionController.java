package com.example.demo.web.rest;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.AppCollection;
import com.example.demo.service.AppCollectionService;

@RestController
@RequestMapping("/api/collection")
public class AppCollectionController {
	private final AppCollectionService appCollectionService;
	
	public AppCollectionController(AppCollectionService appCollectionService) {
		super();
		this.appCollectionService = appCollectionService;
	}

	@GetMapping
	public ResponseEntity<List<AppCollection>> findAll(){
		List<AppCollection> collections = appCollectionService.findAll();
		collections.stream()
		.peek(lightAppCollection);
		
		return ResponseEntity.ok(collections);
	}
	
	@PostMapping
	public ResponseEntity<Void> save(@RequestBody AppCollection appCollection){
		appCollectionService.save(appCollection);
		return ResponseEntity.accepted().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AppCollection> getCollection(@PathVariable Long id){
		AppCollection collection = appCollectionService.findById(id);
		lightAppCollection.accept(collection);
		return ResponseEntity.ok(collection);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCollection(@PathVariable Long id){
		AppCollection appCollection = appCollectionService.findById(id);
		appCollectionService.delete(appCollection);
		return ResponseEntity.accepted().build();
	}
	
	private Consumer<AppCollection> lightAppCollection = (AppCollection collection) ->{
		collection.getDocuments()
		.stream()
		.forEach(document -> {
			document.setElements(null);
		});
		
		collection.getMembers()
		.stream()
		.forEach(member -> {
			member.setPassword(null);
		});
	};
}
