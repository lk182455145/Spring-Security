package com.lk.ss6.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * 资源
 */
@Setter
@Getter
@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 路劲
     */
    private String uri;

    /**
     * 访问方法
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> method;

    @ManyToMany(mappedBy = "resources")
    private Set<Role> roles;
}
