package com.employeeApp.employee.config;

import com.employeeApp.employee.filter.ApiAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new ApiAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers( "/employees/**").authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}