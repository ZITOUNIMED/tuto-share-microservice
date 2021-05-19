package com.example.demo.config.initData;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.util.RoleEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@ConditionalOnProperty(value="init.roles.data")
public class RolesInitDataRunner implements ApplicationRunner {
	private final static Logger logger = LoggerFactory.getLogger(RolesInitDataRunner.class);

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args){
        logger.info("init roles data:");

        Role roleUser = roleRepository.findByName(RoleEnum.ROLE_USER.getName());
        if(roleUser == null){
            roleUser = new Role(RoleEnum.ROLE_USER);
            roleRepository.save(roleUser);
            logger.info("New Role: ROLE_USER");
        }

        Role roleAdmin = roleRepository.findByName(RoleEnum.ROLE_ADMIN.getName());
        if(roleAdmin == null){
            roleAdmin = new Role(RoleEnum.ROLE_ADMIN);
            roleRepository.save(roleAdmin);
            logger.info("New Role: ROLE_ADMIN");
        }

        Role roleSourcer = roleRepository.findByName(RoleEnum.ROLE_SOURCER.getName());
        if(roleSourcer == null){
            roleSourcer = new Role(RoleEnum.ROLE_SOURCER);
            roleRepository.save(roleSourcer);
            logger.info("New Role: ROLE_SOURCER");
        }

        Role roleGuest = roleRepository.findByName(RoleEnum.ROLE_GUEST.getName());
        if(roleGuest == null){
            roleGuest = new Role(RoleEnum.ROLE_GUEST);
            roleRepository.save(roleGuest);
            logger.info("New Role: ROLE_GUEST");
        }
    }
}
