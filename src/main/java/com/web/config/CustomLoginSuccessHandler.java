package com.web.config;

import com.web.entity.User;
import com.web.enums.Role;
import com.web.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();

        Optional<User> user = userRepository.findByEmail(email);
        if(user.get().getRole().equals(Role.ROLE_USER)){
            response.sendRedirect("/");
        }
        if(user.get().getRole().equals(Role.ROLE_ADMIN)){
            response.sendRedirect("/admin/tai-khoan");
        }
    }
}
