package com.mitrais.bootcamp.sb.controller;

import com.mitrais.bootcamp.sb.EntityNotFoundException;
import com.mitrais.bootcamp.sb.HeaderUtil;
import com.mitrais.bootcamp.sb.controller.dto.UserDTO;
import com.mitrais.bootcamp.sb.controller.utils.PaginationUtil;
import com.mitrais.bootcamp.sb.dao.UserDao;
import com.mitrais.bootcamp.sb.domain.User;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final String ENTITY_NAME = "User";
    private final UserDao dao;
    Logger log = LoggerFactory.getLogger(UserController.class);
    private final ResourceAssembler<User, Resource<User>> userResourceAssembler;
    public UserController(UserDao dao, ResourceAssembler<User, Resource<User>> userResourceAssembler) {
        this.dao = dao;
        this.userResourceAssembler = userResourceAssembler;
    }

    //Both @PreAuthorize and @PostAuthorize annotations provide expression-based access control. Hence, predicates can be written using SpEL (Spring Expression Language).
    //@PreAuthorize("hasRole('ROLE_VIEWER')")

    //The @RoleAllowed annotation is the JSR-250’s equivalent annotation of the @Secured annotation.
    //@RolesAllowed({ "ROLE_VIEWER", "ROLE_EDITOR" })

    //The @PreAuthorize(“hasRole(‘ROLE_VIEWER’)”) has the same meaning as @Secured(“ROLE_VIEWER”)
    //@Secured({"ADMIN"})
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<Iterable<UserDTO>> findAll(Pageable pg) throws URISyntaxException{
        final Page<UserDTO> page = dao.findAll(pg).map(UserDTO::new);
        //HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/users");
        return new ResponseEntity<>(page, HttpStatus.OK);

    }

    @GetMapping("all")
    public Iterable<User> findAll2() throws URISyntaxException{
        return dao.findAll();

    }


    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) throws URISyntaxException {
        log.debug("REST request to save User {}", user);
        if(user.getId()!=null){
            return update(user);
        }
        if(dao.findOneByUsername(user.getUsername()).isPresent()){
            return ResponseEntity
                    .badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
                            "userExists", "User with value '"+ user.getUsername()+"' already exists"))
                    .body(null);

        }else{
            User newUser = dao.save(user);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getId()))
                    .headers(HeaderUtil.createAlert( "A User is created with identifier " + newUser.getId(), newUser.getId()))
                    .body(newUser);
        }
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) throws URISyntaxException {
        log.debug("REST request to save User {}", user);
        Optional<User> existingUser = dao.findOneByUsername(user.getUsername());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(user.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "nameExists", "Username already in use")).body(null);
        }
        Optional<User> updatedUser = Optional.of(dao.save(user));

        return ResponseUtil.wrapOrNotFound(updatedUser,
                HeaderUtil.createAlert("A User is updated with identifier " + user.getUsername(), user.getUsername()));
    }

    @Transactional
    @GetMapping("{id}")
    public User findOne(@PathVariable String id)  throws EntityNotFoundException {
        log.debug("REST request to get User by id : {}", id);

        Optional<User> result = dao.findById(id);
        if (!result.isPresent()) {
            throw new EntityNotFoundException(User.class, "id", id);
        }
        result.get().getRole().getPermissions().size();
        return result.get();

    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        log.debug("REST request to delete User: {}", id);
        dao.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A User is deleted with identifier: "+id, id)).build();
    }

}
