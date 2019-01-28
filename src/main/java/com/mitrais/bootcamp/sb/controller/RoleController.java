package com.mitrais.bootcamp.sb.controller;

import com.mitrais.bootcamp.sb.EntityNotFoundException;
import com.mitrais.bootcamp.sb.HeaderUtil;
import com.mitrais.bootcamp.sb.controller.dto.RoleDTO;
import com.mitrais.bootcamp.sb.controller.dto.UserDTO;
import com.mitrais.bootcamp.sb.controller.utils.PaginationUtil;
import com.mitrais.bootcamp.sb.dao.RoleDao;
import com.mitrais.bootcamp.sb.dao.UserDao;
import com.mitrais.bootcamp.sb.domain.Permission;
import com.mitrais.bootcamp.sb.domain.Role;
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
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("api/roles")
public class RoleController {
    private final String ENTITY_NAME = "Role";
    private final RoleDao dao;
    Logger log = LoggerFactory.getLogger(RoleController.class);
    private final ResourceAssembler<User, Resource<User>> userResourceAssembler;
    public RoleController(RoleDao dao, ResourceAssembler<User, Resource<User>> userResourceAssembler) {
        this.dao = dao;
        this.userResourceAssembler = userResourceAssembler;
    }

    @Transactional
    @GetMapping("")
    public ResponseEntity<Iterable<RoleDTO>> findAll(Pageable pg) throws URISyntaxException{
        final Page<RoleDTO> page = dao.findAll(pg).map(RoleDTO::new);
        //HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "api/roles");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody Role role) throws URISyntaxException {
        log.debug("REST request to save Role {}", role);
        if(role.getId()!=null){
            return update(role);
        }
        if(dao.findOneByName(role.getName()).isPresent()){
            return ResponseEntity
                    .badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
                            "roleExists", "Role with value '"+ role.getName()+"' already exists"))
                    .body(null);

        }else{
            Role newRole = dao.save(role);
            return ResponseEntity.created(new URI("/api/roles/" + newRole.getId()))
                    .headers(HeaderUtil.createAlert( "A Role is created with identifier " + newRole.getId(), newRole.getId()))
                    .body(newRole);
        }
    }

    @PutMapping
    public ResponseEntity<Role> update(@RequestBody Role role) throws URISyntaxException {
        log.debug("REST request to save Role {}", role);
        Optional<Role> existingRole = dao.findOneByName(role.getName());
        if (existingRole.isPresent() && (!existingRole.get().getId().equals(role.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "roleExists", "Nama already in use")).body(null);
        }
        Optional<Role> updatedRole = Optional.of(dao.save(role));

        return ResponseUtil.wrapOrNotFound(updatedRole,
                HeaderUtil.createAlert("A Role is updated with identifier " + role.getName(), role.getName()));
    }

    @GetMapping("{id}")
    @Transactional
    public Role findOne(@PathVariable String id)  throws EntityNotFoundException {
        log.debug("REST request to get Role by id : {}", id);

        Optional<Role> result = dao.findById(id);
        if (!result.isPresent()) {
            throw new EntityNotFoundException(Role.class, "id", id);
        }
//        Problem below:
//        Could not write JSON: failed to lazily initialize a collection of role: com.mitrais.bootcamp.sb.domain.Role.permissions,
//        could not initialize proxy - no Session; nested exception is com.fasterxml.jackson.databind.JsonMappingException:
//        failed to lazily initialize a collection of role: com.mitrais.bootcamp.sb.domain.Role.permissions,
//        could not initialize proxy - no Session (through reference chain: com.mitrais.bootcamp.sb.domain.Role[\"permissions\"])
//        The solution is adding Annotation @Transactional on the method and add code in the line below
        result.get().getPermissions().size();
        return result.get();

    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        log.debug("REST request to delete Role: {}", id);
        dao.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A Role is deleted with identifier: "+id, id)).build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalStateException.class})
    public void handle() {
        log.debug("Resource dengan URI tersebut tidak ditemukan");
    }
}
