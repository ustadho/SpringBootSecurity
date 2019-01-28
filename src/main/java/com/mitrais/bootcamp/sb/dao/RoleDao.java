package com.mitrais.bootcamp.sb.dao;

import com.mitrais.bootcamp.sb.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RoleDao extends PagingAndSortingRepository<Role, String> {
    Optional<Role> findOneByName(String name);

}
