package com.example.project;

import com.example.project.model.Role;
import com.example.project.model.User;
import com.example.project.service.RoleService;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
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
		if (roleList.isEmpty()) {
			Role roleAdmin = new Role();
			roleAdmin.setId(1L);
			roleAdmin.setName("ROLE_ADMIN");
			roleService.save(roleAdmin);
			Role roleUser = new Role();
			roleUser.setId(2L);
			roleUser.setName("ROLE_USER");
			roleService.save(roleUser);
		}
		if (users.isEmpty()) {
			User admin = new User();
			Set<Role> roles = new HashSet<>();
			Role roleAdmin = new Role();
			roleAdmin.setId(1L);
			roleAdmin.setName("ROLE_ADMIN");
			roles.add(roleAdmin);
			admin.setEmail("tanphat@gmail.com");
			admin.setPassword("123456");
			admin.setRoles(roles);
			userService.save(admin);
		}
	}
}
