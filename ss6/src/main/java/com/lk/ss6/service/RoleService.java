package com.lk.ss6.service;

import com.lk.ss6.domain.Role;

public interface RoleService {

    /**
     * 为角色添加资源
     * @param id
     * @param resourceId
     * @return
     */
    Role roleAddResource(Long id, Long resourceId);

}
