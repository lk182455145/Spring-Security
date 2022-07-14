package com.lk.ss6.controller;

import com.lk.ss6.domain.Role;
import com.lk.ss6.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PutMapping("{id}/resource/{resourceId}")
    public Role roleAddResource(@PathVariable("id")Long id, @PathVariable("resourceId")Long resourceId){
        return roleService.roleAddResource(id, resourceId);
    }

}
