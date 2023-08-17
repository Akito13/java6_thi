package com.java6.nhom1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("admin/**", "api/**").hasRole("ADMIN")
                                .requestMatchers("user/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("guest/**").hasAnyRole("GUEST")
                                .requestMatchers("home/**", "/").authenticated()
                                .anyRequest().permitAll()
                )
                .exceptionHandling(handler -> handler.accessDeniedPage("/"))
                .formLogin(
                        form -> form
                                .loginPage("/account/loginpage")
                                .loginProcessingUrl("/account/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/home", true)

                )
                .logout(
                        config -> config
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .deleteCookies("JSESSIONID")
                                .logoutUrl("/account/logout")
                )
                .httpBasic(Customizer.withDefaults())
                ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
