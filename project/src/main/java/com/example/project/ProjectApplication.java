package com.example.project;

import com.example.project.model.EnumRole;
import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.service.RoleService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@ComponentScan("com.example")
public class ProjectApplication {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @PostConstruct
    public void init() {
        List<User> users = (List<User>) userService.findAll();
        List<Role> roleList = (List<Role>) roleService.findAll();
//		if (roleList.isEmpty()) {
//			Role roleAdmin = new Role();
//			roleAdmin.setId(1L);
//			roleAdmin.setName(String.valueOf(EnumRole.ROLE_ADMIN));
//			roleService.save(roleAdmin);
//			Role roleDac = new Role();
//			roleDac.setId(2L);
//			roleDac.setName(String.valueOf(EnumRole.ROLE_DAC));
//			roleService.save(roleDac);
//			Role roleAdvertiser = new Role();
//			roleAdvertiser.setId(3L);
//			roleAdvertiser.setName(String.valueOf(EnumRole.ROLE_ADVERTISER));
//			roleService.save(roleAdvertiser);
//		}
//		if (users.isEmpty()) {
//			User user = new User();
//			Role userRole = new Role();
//			userRole.setId(1L);
//			user.setEmail("tanphat@gmail.com");
//			user.setPassword("123456");
//			user.setRole(userRole);
//			userService.save(user);
//		}
//	}
    }
}
