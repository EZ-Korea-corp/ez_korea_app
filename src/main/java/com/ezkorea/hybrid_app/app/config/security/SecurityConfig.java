package com.ezkorea.hybrid_app.app.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        csrf -> csrf
                                .ignoringAntMatchers("/h2-console/**")
                                .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                )
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/")
                )
                .rememberMe(
                        rememberMe -> rememberMe
                                .key("uniqueAndSecret")
                                .tokenValiditySeconds(86400)
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                )
                .exceptionHandling(
                        handler -> handler
                                .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeRequests(
                        request -> request
                                .antMatchers("/signup", "/login").anonymous()
                                .antMatchers("/assets/**").permitAll()
                                .antMatchers("**/manager/**").hasAuthority("ROLE_MANAGER")
                                .anyRequest().authenticated()
                );
        return http.build();
    }
}
