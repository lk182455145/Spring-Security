package com.lk.ss6.service.impl;

import com.lk.ss6.domain.Resource;
import com.lk.ss6.domain.Role;
import com.lk.ss6.exception.DataNotException;
import com.lk.ss6.respotiry.ResourceResposity;
import com.lk.ss6.respotiry.RoleRespotiry;
import com.lk.ss6.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRespotiry roleRespotiry;

    private final ResourceResposity resourceResposity;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"role_resource"}, key = "'role_' + #id + '_resource'", allEntries = true)
    public Role roleAddResource(Long id, Long resourceId) {
        Role role = roleRespotiry.findById(id).orElseThrow(DataNotException::new);
        Resource resource = resourceResposity.findById(resourceId).orElseThrow(DataNotException::new);
        role.getResources().add(resource);
        return roleRespotiry.save(role);
    }
}
