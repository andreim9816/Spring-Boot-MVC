package com.example.project.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Profile("h2")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_DOCTOR = "doctor";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin_1")
                .password(passwordEncoder().encode("12345"))
                .roles(ROLE_ADMIN)
                .and()
                .withUser("doctor_1")
                .password(passwordEncoder().encode("12345"))
                .roles(ROLE_DOCTOR);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .anyRequest().authenticated()
                .antMatchers("/departments/new", "/departments/{^[0-9]+}/edit").hasRole(ROLE_ADMIN)
                .antMatchers("/departments/{^[0-9]+}").permitAll()
                .antMatchers("/departments").permitAll()

                .antMatchers("/patients/new", "/patients/{^[0-9]+}/edit").hasRole(ROLE_ADMIN)
                .antMatchers("/patients/{^[0-9]+}").permitAll()
                .antMatchers("/patients").permitAll()

                .antMatchers("/doctors/new", "/doctors/{^[0-9]+}/edit").hasRole(ROLE_ADMIN)
                .antMatchers("/doctors/{^[0-9]+}").permitAll()
                .antMatchers("/doctors").permitAll()

                .antMatchers("/medications/new", "/medications/{^[0-9]+}/edit").hasRole(ROLE_ADMIN)
                .antMatchers("/medications/{^[0-9]+}").permitAll()
                .antMatchers("/medications").permitAll()

                .antMatchers("/consults/new", "/consults/{^[0-9]+}/edit").hasRole(ROLE_ADMIN)
                .antMatchers("/consults/{^[0-9]+}").permitAll()
                .antMatchers("/consults").permitAll()

                .antMatchers("/**/bootstrap/**").permitAll()

                .and()
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/authUser")
                .permitAll()
                .and()
                .logout().permitAll()
                .logoutSuccessUrl("/index")
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling().accessDeniedPage("/access_denied");

        // http.headers().frameOptions().sameOrigin();

    }
}

