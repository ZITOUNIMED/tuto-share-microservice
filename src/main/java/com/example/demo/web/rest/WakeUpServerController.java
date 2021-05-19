package com.example.demo.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wake-up-server")
public class WakeUpServerController {

	@GetMapping
	public void wakeUp(){
	}

}
