package com.web.config;


import com.web.exception.UserNotActivatedException;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig  {

    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/gio-hang").hasAuthority("ROLE_USER")
                        .requestMatchers("/checkout").hasAuthority("ROLE_USER")
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login?expired") // Redirect nếu session bị mất
                        .maximumSessions(1) // Chặn login cùng lúc ở nhiều nơi (optional)
                        .maxSessionsPreventsLogin(false) // Cho login mới, đá login cũ ra
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureHandler(customAuthenticationFailureHandler)
                        .successHandler(customLoginSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL xử lý logout
                        .logoutSuccessUrl("/login")  // Chuyển hướng sau khi logout thành công
                        .invalidateHttpSession(true)  // Xóa session
                        .deleteCookies("JSESSIONID")  // Xóa cookie nếu cần
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {
                                    if (authException instanceof UserNotActivatedException) {
                                        String email = ((UserNotActivatedException) authException).getEmail();
                                        System.out.println("active exception");
                                        response.sendRedirect("/confirm?email="+email);
                                    } else {
                                        System.out.println("another exception");
                                        response.sendRedirect("/login?error");
                                    }
                                })
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<com.web.entity.User> user = userRepository.findByEmail(username);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException("User " + username + " was not found in the database");
            }
            if (!user.get().getActived()) {
                throw new UserNotActivatedException(user.get().getEmail(), user.get().getEmail());
            }
            List<String> roles = new ArrayList<>();
            roles.add(user.get().getRole().toString());

            List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
            if (roles != null) {
                for (String role : roles) {
                    GrantedAuthority authority = new SimpleGrantedAuthority(role);
                    grantList.add(authority);
                }
            }

            String password = user.get().getPassword();
            UserDetails userDetails = (UserDetails) new User(username, //
                    password, grantList);
            return userDetails;
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
