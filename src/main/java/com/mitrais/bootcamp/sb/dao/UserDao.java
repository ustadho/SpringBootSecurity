package com.mitrais.bootcamp.sb.dao;

import com.mitrais.bootcamp.sb.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends PagingAndSortingRepository<User, String> {
    Optional<User> findOneByUsername(String username);

//    @Override
//    @Query("from User u " +
//            "join u.role r " +
//            "left join fetch r.permissionSet ps ")
//    Page<User> findAll(Pageable pageable);
}
