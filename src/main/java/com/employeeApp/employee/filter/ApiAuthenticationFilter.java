package com.employeeApp.employee.filter;

import com.employeeApp.employee.entity.ApiKeyAuthentication;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class ApiAuthenticationFilter implements Filter {
    static final private String AUTH_KEY = "EMPLOYEE-API-KEY";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        if(request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            String apiKey = getApiKey((HttpServletRequest) request);
            if(apiKey != null) {
                if(apiKey.equals("admin")) {
                    ApiKeyAuthentication apiToken = new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
                    SecurityContextHolder.getContext().setAuthentication(apiToken);
                } else {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.setStatus(401);
                    httpResponse.getWriter().write("Invalid API Key");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getApiKey(HttpServletRequest httpRequest) {
        String apiKey = null;

        String authHeader = httpRequest.getHeader(AUTH_KEY);
        if(authHeader != null) {
            apiKey = authHeader;
        }

        return apiKey;
    }
}