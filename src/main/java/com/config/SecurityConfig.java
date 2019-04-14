package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailservice myUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/sign")
                .failureUrl("/sign?error")
                .defaultSuccessUrl("/haupt")
                .and().logout().logoutSuccessUrl("/haupt?logout")
                .and()
                .exceptionHandling().accessDeniedPage("/templates/WEB-INF/pages/deniedPage.html");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        return authProvider;
    }
}
