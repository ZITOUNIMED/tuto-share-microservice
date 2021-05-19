package com.example.demo.config.security.permissions.services.impl;

import java.util.Set;
import java.util.function.Predicate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.example.demo.config.security.permissions.services.AppPermissionService;
import com.example.demo.entity.AppCollection;
import com.example.demo.util.AppPermissionTypes;

@Component
public class DocumentCollectionPermissionService implements AppPermissionService<AppCollection, String>{

	@Override
	public boolean hasPermission(Authentication authentication, AppCollection collection, String permission) {
		User user = (User) authentication.getPrincipal();
		Predicate<Set<com.example.demo.entity.User>> checkMember = (Set<com.example.demo.entity.User> members) -> {
			return members.stream().filter(member -> member.getUsername().equals(user.getUsername()))
			.findAny().isPresent();
		};
		
		switch(permission){
		case AppPermissionTypes.READ:
			return (collection.getOwnerUsername().equals(user.getUsername()) ||
					checkMember.test(collection.getMembers()));
		}
		return false;
	}
	
}
