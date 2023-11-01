package com.newbarams.ajaja.module.user.presentation;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@PostMapping("/hello")
	@ResponseStatus(OK)
	public String hello() {
		return "world";
	}
}
