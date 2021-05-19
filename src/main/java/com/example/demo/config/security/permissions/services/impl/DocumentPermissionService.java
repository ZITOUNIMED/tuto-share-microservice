package com.example.demo.config.security.permissions.services.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.example.demo.config.security.permissions.services.AppPermissionService;
import com.example.demo.entity.Document;
import com.example.demo.util.AppPermissionTypes;
import com.example.demo.util.ConfidentialityEnum;

@Component
public class DocumentPermissionService implements AppPermissionService<Document, String>{

	@Override
	public boolean hasPermission(Authentication authentication, Document document, String permission) {
		User user = (User) authentication.getPrincipal();
		switch(permission){
			case AppPermissionTypes.OWNER:
				return user.getUsername().equals(document.getOwnerUsername());
			case AppPermissionTypes.PUBLIC:
				return ConfidentialityEnum.PUBLIC.getName().equals(document.getConfidentiality());
		}
		return false;
	}
}
