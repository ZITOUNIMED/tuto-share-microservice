package com.example.demo.config.security.permissions.services;

import org.springframework.security.core.Authentication;

public interface AppPermissionService<TargetType, AppPermissionType> {
	boolean hasPermission(Authentication authentication, TargetType targetObj, AppPermissionType appPermission);
}
