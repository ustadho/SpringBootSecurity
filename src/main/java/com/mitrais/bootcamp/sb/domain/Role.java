package com.mitrais.bootcamp.sb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "c_role")
@Data
public class Role {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @NotEmpty
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(
            name="c_role_permission",
            joinColumns=@JoinColumn(name="role_id", nullable=false),
            inverseJoinColumns=@JoinColumn(name="permission_id", nullable=false)
    )
    private Set<Permission> permissions = new HashSet<Permission>();

    public Role(){}

    public Role(String id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Role(String id, String name, String description, Set<Permission> ps){
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = ps;
    }
}
