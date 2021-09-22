package com.wg_planner.backend.security;

import com.wg_planner.backend.Service.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";
    private static final String REGISTER_URL = "/register";
    private static final String REGISTER_FORM = "/register_form";
    private static final String CREATE_FLOOR_URL = "/create_floor";
    private static final String OVERVIEW_URL = "/overview";
    @Autowired
    AccountDetailsService accountDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .requestCache().requestCache(new CustomRequestCache())
                .and().authorizeRequests()
                .antMatchers(REGISTER_URL).permitAll()
                .antMatchers(REGISTER_FORM).denyAll()
                .antMatchers(CREATE_FLOOR_URL).permitAll()
                .antMatchers(OVERVIEW_URL).permitAll()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage(OVERVIEW_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                .and()
                .rememberMe()
//                .rememberMeCookieName("wg-planer")
                .userDetailsService(this.userDetailsService())
//                .useSecureCookie(true)
                .alwaysRemember(true)
                .tokenValiditySeconds(2419200);//28 days
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/new_device/**",
                "/update_device/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/h2-console/**");
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return accountDetailsService;
    }
}