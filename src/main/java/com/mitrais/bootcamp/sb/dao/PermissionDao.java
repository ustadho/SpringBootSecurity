package com.mitrais.bootcamp.sb.dao;

import com.mitrais.bootcamp.sb.domain.Permission;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PermissionDao extends PagingAndSortingRepository<Permission, String> {
    public Optional<Permission> findOneByValue(String value);

}
