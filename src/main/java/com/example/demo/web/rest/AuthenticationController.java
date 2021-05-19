package com.example.demo.web.rest;

import com.example.demo.config.security.jwt.JwtTokenProvider;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.RegistrationRulesTypeEnum;
import com.example.demo.util.RoleEnum;
import com.example.demo.web.rest.request.SignInRequest;
import com.example.demo.web.rest.request.SignUpRequest;
import com.example.demo.web.rest.response.RegistrationRule;
import com.example.demo.web.rest.response.SignInResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@PostMapping("/signin")
	public ResponseEntity<SignInResponse> signin(@RequestBody SignInRequest request) {
		try {
			String username = request.getUsername();
			String password = request.getPassword();

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			User user = this.userRepository.findByUsername(username);
			if (user == null) {
				throw new UsernameNotFoundException("Username " + username + "not found");
			}

			List<String> roles = user.getRoles().stream()
					.map(role -> role.getName())
					.collect(Collectors.toList());
			
			String token = jwtTokenProvider.createToken(username, roles);
			SignInResponse signInResponse = SignInResponse.builder()
					.username(username)
					.token(token).build();
			return ResponseEntity.ok(signInResponse);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid username/password supplied");
		}
	}

	@GetMapping("registration-rules")
	public ResponseEntity registrationRules() {
		List<RegistrationRule> rules = new ArrayList<>();
		rules.add(RegistrationRule.builder().type(RegistrationRulesTypeEnum.MANDATORY)
				.value("A digit must occur at least once.").build());
		rules.add(RegistrationRule.builder().type(RegistrationRulesTypeEnum.MANDATORY)
				.value("A lower case letter must occur at least once.").build());
		rules.add(RegistrationRule.builder().type(RegistrationRulesTypeEnum.MANDATORY)
				.value("An upper case letter must occur at least once.").build());
		rules.add(RegistrationRule.builder().type(RegistrationRulesTypeEnum.MANDATORY)
				.value("A special character must occur at least once.").build());
		rules.add(RegistrationRule.builder().type(RegistrationRulesTypeEnum.MANDATORY)
				.value("No whitespace allowed in the entire string.").build());
		rules.add(RegistrationRule.builder().type(RegistrationRulesTypeEnum.MANDATORY)
				.value("Anything, at least 10 places though.").build());
		return ResponseEntity.ok(rules);
	}

	@PostMapping("/signup")
	public ResponseEntity signup(@RequestBody SignUpRequest request) {
		try {
			String username = request.getUsername();
			String password = request.getPassword();
			String passwordConfirm = request.getPasswordConfirm();

			User existingUser = userRepository.findByUsername(username);
			if (existingUser != null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already user!");
			}

			// a digit must occur at least once
			// a lower case letter must occur at least once
			// an upper case letter must occur at least once
			// a special character must occur at least once
			// no whitespace allowed in the entire string
			// anything, at least 10 places though
			Pattern passwordPattern = Pattern
					.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$)(?=.*[@#$%^&+=]).{10,}$");
			Matcher m = passwordPattern.matcher(password);
			if (!m.matches()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password!");
			}

			if (!password.equals(passwordConfirm)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords are different!");
			}

			Role role = roleRepository.findByName(RoleEnum.ROLE_USER.getName());

			User user = new User();
			user.setUsername(username);
			user.setPassword(passwordEncoder.encode(password));
			user.setRoles(Arrays.asList(role));
			user.setEnable(false);
			user.setFirstname(request.getFirstname());
			user.setLastname(request.getLastname());
			user.setEmail(request.getEmail());

			userRepository.save(user);

			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid cridentials!");
		}
	}
}
