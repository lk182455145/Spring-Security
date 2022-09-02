package com.lk.ss6.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

/**
 * 角色
 */
@Setter
@Getter
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色
     */
    private String authority;

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 资源
     */
    @ManyToMany
    @JoinTable(name = "role_resource_",
            joinColumns = {@JoinColumn(name = "role_id_", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "resource_id_", referencedColumnName = "id")}
    )
    @JsonIgnore
    private Set<Resource> resources;
}
