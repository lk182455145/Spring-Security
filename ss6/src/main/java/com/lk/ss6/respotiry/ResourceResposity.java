package com.lk.ss6.respotiry;

import com.lk.ss6.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceResposity extends JpaRepository<Resource, Long> {

    List<Resource> findByRoles_Id(Long roleId);

    List<Resource> findByRoles_Authority(String authority);

}
