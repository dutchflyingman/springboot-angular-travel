package com.afkl.travel.exercise.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${user.name}")
    String userName;

    @Value("${user.password}")
    String password;

    @Value("${admin.username}")
    String adminName;

    @Value("${admin.password}")
    String adminPassword;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/travel/locations/**/").hasRole("USER")
                .antMatchers("/h2/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        //the h2 console worked once the csrf was disabled
        http.headers().frameOptions().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        //TODO: Move the credentials to resources file and use the bean password encode to encode the password instead noop
        auth.inMemoryAuthentication()
                .withUser(adminName)
                .password(("{noop}"+password))
                .roles("ADMIN").and()
                .withUser("someuser")
                .password(("{noop}psw"))
                .roles("USER");

    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        //@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*",allowCredentials = "true")
        //NOTE: This is done for global configuration, however it is advised to override using cors mapping at method level/resource level rules
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        //Wildcard is giving problem for headers -Investigate to configure the below headers
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Access-Control-Allow-Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin", "Content-Type", "Authorization"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
