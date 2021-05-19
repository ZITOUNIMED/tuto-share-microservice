package com.example.demo.service.impl;

import com.example.demo.entity.AppCollection;
import com.example.demo.repository.AppCollectionRepository;
import com.example.demo.service.AppCollectionService;
import com.example.demo.util.AppPermissionTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppCollectionServiceImpl implements AppCollectionService {
	private final AppCollectionRepository appCollectionRepository;
	
	public AppCollectionServiceImpl(AppCollectionRepository appCollectionRepository) {
		super();
		this.appCollectionRepository = appCollectionRepository;
	}

	@Override
	public void save(AppCollection appCollection) {
		appCollectionRepository.save(appCollection);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, '"+AppPermissionTypes.READ+"') or hasRole('ROLE_ADMIN')")
	public List<AppCollection> findAll() {
		return appCollectionRepository.findAll();
	}

	@Override
	public Page<AppCollection> findAll(Pageable pageableRequest) {
		return null; // TODO
	}

	@Override
	public AppCollection findById(Long id) {
		return appCollectionRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteById(Long id) {
		appCollectionRepository.deleteById(id);
	}

	@Override
	public void saveAll(List<AppCollection> list) {
		appCollectionRepository.saveAll(list);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, '"+AppPermissionTypes.MY_FAVORITE+"')")
	@Override
	public List<AppCollection> findFavoriteCollections() {
		return new ArrayList<>();//appCollectionRepository.findAll();
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN') or #appCollection.ownerUsername == authentication.principal.username")
	public void delete(AppCollection appCollection) {
		appCollectionRepository.delete(appCollection);
	}
}
