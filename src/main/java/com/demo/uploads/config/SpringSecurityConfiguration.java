package com.demo.uploads.config;

import com.demo.uploads.feature.user.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfiguration {

    @Order(2)
    @Configuration
    @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
    @RequiredArgsConstructor
    public static class AuthorizedSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final PasswordHelper passwordHelper;
        private final MyUserDetailsService authService;

        @Override
        public void configure(WebSecurity web) {
            String[] resources = {"/", "/**/*.html", "/**/*.js", "/**/*.css", "/**/*.ts", "/fonts/**"};
            String[] swagger = {"swagger-ui.html", "/configuration/**", "/webjars/**", "/swagger-resources/**", "/v2/api-docs"};
            String[] ignored = ArrayUtils.addAll(resources, swagger);
            web.ignoring().antMatchers(ignored);
        }

        @SneakyThrows
        @Override
        protected void configure(HttpSecurity http) {
            http
                    .requestMatchers().antMatchers("/api/**")
                    .and()
                    .cors()
                    .and()
                    .headers()
                    .disable()
                    .csrf()
                    .disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .httpBasic()
            ;
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(authenticationProvider());
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(authService);
            authProvider.setPasswordEncoder(passwordHelper);
            return authProvider;
        }
    }

    @Order(1)
    @Configuration
    @RequiredArgsConstructor
    public static class PublicSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(WebSecurity web) {
            String[] resources = {"/", "/**/*.html", "/**/*.js", "/**/*.css", "/**/*.ts", "/fonts/**"};
            String[] swagger = {"swagger-ui.html", "/configuration/**", "/webjars/**", "/swagger-resources", "/v2/api-docs"};
            String[] ignored = ArrayUtils.addAll(resources, swagger);
            web.ignoring().antMatchers(ignored);
        }

        @SneakyThrows
        @Override
        protected void configure(HttpSecurity http) {
            http
                    .requestMatchers().antMatchers("/register")
                    .and()
                    .cors()
                    .and()
                    .headers()
                    .disable()
                    .csrf()
                    .disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .anyRequest().permitAll()
            ;
        }

    }
}

