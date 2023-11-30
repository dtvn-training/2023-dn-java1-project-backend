package com.example.project.service;

import com.example.project.model.Role;

public interface RoleService extends GeneralService<Role>{
    Role findByName(String name);
}
