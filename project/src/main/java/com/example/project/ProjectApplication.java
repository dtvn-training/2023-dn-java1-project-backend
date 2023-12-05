package com.example.project;

import com.example.project.service.IRoleService;
import com.example.project.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectApplication {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

}

