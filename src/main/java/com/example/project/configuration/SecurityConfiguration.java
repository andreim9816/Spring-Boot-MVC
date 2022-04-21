package com.example.project.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
//@Profile("h2")
//@Profile("mysql")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String ADMIN = "ADMIN";
    public static final String DOCTOR = "DOCTOR";
    public static final String ROLE_ADMIN = "ROLE_" + ADMIN;
    public static final String ROLE_DOCTOR = "ROLE_" + DOCTOR;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin_1")
//                .password(passwordEncoder().encode("123456"))
//                .roles(ROLE_ADMIN)
//                .and()
//                .withUser("doctor_1")
//                .password(passwordEncoder().encode("123456"))
//                .roles(ROLE_DOCTOR);
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .anyRequest().authenticated()
                .antMatchers("/h2-console/login.do*").permitAll()

                .antMatchers("/departments/new", "/departments/{^[0-9]+}/edit").hasRole(ADMIN)
                .antMatchers("/departments/{^[0-9]+}").permitAll()
                .antMatchers("/departments").permitAll()

                .antMatchers("/patients/new", "/patients/{^[0-9]+}/edit").hasRole(ADMIN)
                .antMatchers("/patients/{^[0-9]+}").hasAnyRole(ADMIN, DOCTOR)
                .antMatchers("/patients").hasAnyRole(ADMIN, DOCTOR)

//                .antMatchers("/doctors/new", "/doctors/{^[0-9]+}/edit").hasRole(ADMIN)
                .antMatchers("/doctors/{^[0-9]+}/edit").hasAnyRole(DOCTOR, ADMIN)
                .antMatchers("/doctors/my-profile").hasRole(DOCTOR)
                .antMatchers("/doctors/{^[0-9]+}").permitAll()
                .antMatchers("/doctors").permitAll()

                .antMatchers("/medications/new", "/medications/{^[0-9]+}/edit").hasRole(ADMIN)
                .antMatchers("/medications/{^[0-9]+}").permitAll()
                .antMatchers("/medications").permitAll()

                .antMatchers("/consults/{^[0-9]+}/edit").hasAnyRole(DOCTOR, ADMIN)
                .antMatchers("/consults/new").hasAnyRole(DOCTOR, ADMIN)
                .antMatchers("/consults/{^[0-9]+}").hasAnyRole(DOCTOR, ADMIN)
                .antMatchers("/consults/my-consults").hasRole(DOCTOR)
                .antMatchers("/consults").hasAnyRole(DOCTOR, ADMIN)

                .antMatchers("/**/bootstrap/**").permitAll()

                .and()

                .headers().frameOptions().disable()
                .and()
                .csrf().disable()

                .formLogin().loginPage("/login")
                .loginProcessingUrl("/authUser")
                .permitAll()
                .and()
                .logout().permitAll()
                .logoutSuccessUrl("/index")
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling().accessDeniedPage("/access-denied");

    }
}

