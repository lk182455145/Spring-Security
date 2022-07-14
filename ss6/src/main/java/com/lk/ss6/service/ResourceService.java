package com.lk.ss6.service;

import com.lk.ss6.domain.Resource;

import java.util.List;

public interface ResourceService {

    List<Resource> findByRolesId(Long roleId);

    List<Resource> findByRolesAuthority(String authority);
}
