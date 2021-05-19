package com.example.demo.config.security.permissions;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.config.security.permissions.services.AppPermissionService;
import com.example.demo.entity.Document;
import com.example.demo.entity.AppCollection;

@Component
public class AppPermissionEvaluator implements PermissionEvaluator {
	private final AppPermissionService<Document, String> documentPermissionService;
	private final AppPermissionService<AppCollection, String> documentCollectionPermissionService;

	public AppPermissionEvaluator(AppPermissionService<Document, String> documentPermissionService,
			AppPermissionService<AppCollection, String> documentCollectionPermissionService) {
		this.documentPermissionService = documentPermissionService;
		this.documentCollectionPermissionService = documentCollectionPermissionService;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetObj, Object permission) {
		if(targetObj instanceof Document) {
			return documentPermissionService.hasPermission(authentication, (Document) targetObj, (String) permission);
		} else if(targetObj instanceof AppCollection){
			return documentCollectionPermissionService.hasPermission(authentication, (AppCollection) targetObj, (String) permission);
		}
		
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		
		return false;
	}

}
