package com.lk.ss6.authorization;

import com.lk.ss6.domain.Resource;
import com.lk.ss6.domain.Role;
import com.lk.ss6.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final ResourceService resourceService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        //没有角色的禁止访问
        if (authentication.get().getAuthorities().isEmpty()) {
            return new AuthorizationDecision(false);
        }

        //匿名账户禁止访问 ROLE_ANONYMOUS
        if ("anonymousUser".equals(authentication.get().getName())) {
            return new AuthorizationDecision(false);
        }

        // 获取未被禁用的角色ID
        Set<Long> roleIds = authentication.get().getAuthorities().stream()
                .filter(role -> role instanceof Role)
                .filter(role -> ((Role) role).isEnabled())
                .map(role -> ((Role) role).getId())
                .collect(Collectors.toSet());

        // 判断角色是否存在
        if (roleIds.isEmpty()) {
            return new AuthorizationDecision(false);
        }

        // 根据角色获取所有资源
        Set<Resource> resources = roleIds.stream()
                .map(resourceService::findByRolesId)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        //根据资源来判断路劲是否匹配
        return new AuthorizationDecision(matches(resources,context.getRequest()));
    }


    private boolean matches(Set<Resource> resources, HttpServletRequest request) {
        for (Resource resource : resources) {
            final String uri = resource.getUri();
            if (resource.getMethod().isEmpty()) {
                if (new AntPathRequestMatcher(uri).matches(request)) {
                    return true;
                }
            } else {
                for (String method : resource.getMethod()) {
                    if (new AntPathRequestMatcher(uri, method).matches(request)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
