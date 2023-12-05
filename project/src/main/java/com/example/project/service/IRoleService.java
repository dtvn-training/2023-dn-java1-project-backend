package com.example.project.service;

import com.example.project.model.Role;

public interface IRoleService extends IGeneralService<Role> {
    Role findByName(String name);
}
