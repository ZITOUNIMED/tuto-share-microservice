package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserService extends CrudService<User, Long>{
	User findByUsername(String username);
}
