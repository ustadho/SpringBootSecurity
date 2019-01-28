package com.mitrais.bootcamp.sb.controller.dto;

import com.mitrais.bootcamp.sb.domain.Permission;
import com.mitrais.bootcamp.sb.domain.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class RoleDTO implements Serializable {
    private String id;
    private String name;
    private String description;
    private Set<Permission> permissions;

    public RoleDTO(){}

    public RoleDTO(Role role){
        this(role.getId(), role.getName(), role.getDescription(), role.getPermissions());
    }
    public RoleDTO(String id, String name, String description, Set<Permission> ps){
        this.id = id;
        this.name = name;
        this.description = description;
//        ps.size();
//        this.permissions = ps;
//        this.permissions = ps.stream().map(permission -> {
//          Permission p = new Permission();
//          p.setId(permission.getId());
//          p.setLabel(permission.getLabel());
//          p.setValue(permission.getValue());
//          p.setRoles(null);
//          return p;
//        }).collect(Collectors.toSet());
    }
}
