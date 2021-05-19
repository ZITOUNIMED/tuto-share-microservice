package com.example.demo.web.rest;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/user")
public class UserController {

	private final UserService userService;

    public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping
    public ResponseEntity<List<User>> findAll(){
    	List<User> users = userService.findAll()
    			.stream()
    			.map(user -> {
    				user.setPassword(null);
    				return user;
    			})
    			.collect(Collectors.toList());
    	return ResponseEntity.ok(users);
    }

    @GetMapping("/by-username/{username}")
	public ResponseEntity<User> findByUsername(@PathVariable String username){
    	User user = userService.findByUsername(username);
    	if(user!=null){
    		user.setPassword(null); // Don't return password in the response
		}
    	return ResponseEntity.ok(user);
	}
    
    @PostMapping
    public ResponseEntity<Void> saveUser(@RequestBody User user){
    	userService.save(user);
    	return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
    	userService.deleteById(id);
    	return ResponseEntity.accepted().build();
    }
}
