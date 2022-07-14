package com.lk.ss6.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lk.ss6.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    private RequestCache requestCache = new HttpSessionRequestCache();

    /**
     * 获取上次地址进行跳转，如果没有返回用户姓名
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        if (savedRequest == null) {
            NotRedirect(response, authentication);
        } else {
            String url = savedRequest.getRedirectUrl();
            if (new URL(url).getPath().replaceAll("/", "").length() == 0) {
                NotRedirect(response, authentication);
            } else {
                clearAuthenticationAttributes(request);
                getRedirectStrategy().sendRedirect(request, response, url);
            }
        }
    }

    private void NotRedirect(HttpServletResponse response, Authentication authentication) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setCharacterEncoding("UTF-8");
        if (authentication.getPrincipal() instanceof User) {
            response.getWriter().println(objectMapper.writeValueAsString("你好：" + ((User) authentication.getPrincipal()).getName()));
        } else {
            response.getWriter().println(objectMapper.writeValueAsString("你好：" + authentication.getName()));
        }
    }
}
