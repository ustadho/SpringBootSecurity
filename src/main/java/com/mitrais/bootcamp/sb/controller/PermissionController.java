package com.mitrais.bootcamp.sb.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.mitrais.bootcamp.sb.EntityNotFoundException;
import com.mitrais.bootcamp.sb.HeaderUtil;
import com.mitrais.bootcamp.sb.dao.PermissionDao;
import com.mitrais.bootcamp.sb.domain.Permission;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.Resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("api/permissions")
public class PermissionController {
    private final String ENTITY_NAME = "Permission";
    private final PermissionDao dao;
    Logger log = LoggerFactory.getLogger(PermissionController.class);
    private final ResourceAssembler<Permission, Resource<Permission>> permissionResourceAssembler;
    public PermissionController(PermissionDao dao, ResourceAssembler<Permission, Resource<Permission>> permissionResourceAssembler) {
        this.dao = dao;
        this.permissionResourceAssembler = permissionResourceAssembler;
    }

    @GetMapping("")
    public Iterable<Permission> findAll(){
        return dao.findAll();
    }

    @PostMapping
    public ResponseEntity<Permission> create(@RequestBody Permission permission) throws URISyntaxException {
        log.debug("REST request to save Permission {}", permission);
        if(permission.getId()!=null){
            return update(permission);
        }
        if(dao.findOneByValue(permission.getValue()).isPresent()){
            return ResponseEntity
                    .badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
                            "permissionValueExists", "Permission with value '"+ permission.getValue()+"' already exists"))
                    .body(null);

        }else{
            Permission newPermission = dao.save(permission);
            return ResponseEntity.created(new URI("/permissions/" + newPermission.getId()))
                    .headers(HeaderUtil.createAlert( "A Permission is created with identifier " + newPermission.getId(), newPermission.getId()))
                    .body(newPermission);
        }
    }

    @PutMapping
    public ResponseEntity<Permission> update(@RequestBody Permission permission) throws URISyntaxException {
        log.debug("REST request to save Permission {}", permission);
        Optional<Permission> existingPermission = dao.findOneByValue(permission.getValue());
        if (existingPermission.isPresent() && (!existingPermission.get().getId().equals(permission.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "valueExists", "Value already in use")).body(null);
        }
        Optional<Permission> updatedUser = Optional.of(dao.save(permission));

        return ResponseUtil.wrapOrNotFound(updatedUser,
                HeaderUtil.createAlert("A Permission is updated with identifier " + permission.getValue(), permission.getValue()));
    }

    @GetMapping("{id}")
    public Permission getPermission(@PathVariable String id)  throws EntityNotFoundException {
        log.debug("REST request to get Permission by id : {}", id);

        Optional<Permission> result = dao.findById(id);
        if (!result.isPresent()) {
            throw new EntityNotFoundException(Permission.class, "id", id);
        }
        return result.get();

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        log.debug("REST request to delete Permission: {}", id);
        dao.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A Permission is deleted with identifier: "+id, id)).build();
    }

}
