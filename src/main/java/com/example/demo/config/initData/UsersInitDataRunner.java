package com.example.demo.config.initData;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.RoleEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@ConditionalOnProperty(value="init.users.data")
@ConditionalOnBean(RolesInitDataRunner.class)
@Order(2)
public class UsersInitDataRunner implements ApplicationRunner {
	private final static Logger logger = LoggerFactory.getLogger(UsersInitDataRunner.class);
	
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args){
        logger.info("init users data.........");

        Role roleUser = roleRepository.findByName(RoleEnum.ROLE_USER.getName());
        Role roleAdmin = roleRepository.findByName(RoleEnum.ROLE_ADMIN.getName());
        Role roleSourcer = roleRepository.findByName(RoleEnum.ROLE_SOURCER.getName());
		Role roleGuest = roleRepository.findByName(RoleEnum.ROLE_GUEST.getName());

        User admin = User.builder()
        		.username("admin")
        		.password(passwordEncoder.encode("admin"))
        		.firstname("admin firstname")
        		.lastname("admin lastname")
        		.enable(true)
        		.roles(Arrays.asList(roleAdmin, roleUser, roleSourcer))
        		.build();

        userRepository.save(admin);
        logger.info("New user "+ admin);
        
        User user1 = User.builder()
        		.username("user1")
        		.password(passwordEncoder.encode("user1"))
        		.firstname("user1 firstname")
        		.lastname("user1 lastname")
        		.enable(true)
        		.roles(Arrays.asList(roleUser))
        		.build();

        userRepository.save(user1);
        logger.info("New user "+ user1);
        
        User user2 = User.builder()
        		.username("user2")
        		.password(passwordEncoder.encode("user2"))
        		.firstname("user2 firstname")
        		.lastname("user2 lastname")
        		.enable(true)
        		.roles(Arrays.asList(roleUser))
        		.build();

        userRepository.save(user2);
        logger.info("New user "+ user2);
        
        User user3 = User.builder()
        		.username("user3")
        		.password(passwordEncoder.encode("user3"))
        		.firstname("user3 firstname")
        		.lastname("user3 lastname")
        		.enable(true)
        		.roles(Arrays.asList(roleUser))
        		.build();

        userRepository.save(user3);
        logger.info("New user "+ user3);
        
        User sourcer1 = User.builder()
        		.username("sourcer1")
        		.password(passwordEncoder.encode("sourcer1"))
        		.firstname("sourcer1 firstname")
        		.lastname("sourcer1 lastname")
        		.enable(true)
        		.roles(Arrays.asList(roleSourcer, roleUser))
        		.build();

        userRepository.save(sourcer1);
        logger.info("New user "+ sourcer1);
        
        User sourcer2 = User.builder()
        		.username("sourcer2")
        		.password(passwordEncoder.encode("sourcer2"))
        		.firstname("sourcer2 firstname")
        		.lastname("sourcer2 lastname")
        		.enable(true)
        		.roles(Arrays.asList(roleSourcer, roleUser))
        		.build();

        userRepository.save(sourcer2);
        logger.info("New user "+ sourcer2);

		User guest = User.builder()
				.username("guest")
				.password(passwordEncoder.encode("guest"))
				.firstname("Guest")
				.lastname("Guest")
				.enable(true)
				.roles(Arrays.asList(roleGuest))
				.build();

		userRepository.save(guest);
		logger.info("New user "+ guest);
        
    }
}
