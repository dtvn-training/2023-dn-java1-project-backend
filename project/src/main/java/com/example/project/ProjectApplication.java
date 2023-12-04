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
public class ProjectApplication {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

}

