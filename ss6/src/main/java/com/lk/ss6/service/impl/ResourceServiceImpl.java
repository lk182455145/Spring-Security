package com.lk.ss6.service.impl;

import com.lk.ss6.service.ResourceService;
import com.lk.ss6.domain.Resource;
import com.lk.ss6.respotiry.ResourceResposity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceResposity resourceResposity;

    @Override
    @Cacheable(cacheNames = {"role_resource"}, key = "'role_' + #roleId + '_resource'", sync = true)
    public List<Resource> findByRolesId(Long roleId) {
        System.out.println("调用得cache-key:" + roleId);
        return resourceResposity.findByRoles_Id(roleId);
    }

    @Override
    public List<Resource> findByRolesAuthority(String authority) {
        return resourceResposity.findByRoles_Authority(authority);
    }
}
