package com.web.config;


import com.web.entity.User;
import com.web.exception.UserNotActivatedException;
import com.web.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof UserNotActivatedException) {
            String email = ((UserNotActivatedException) exception).getEmail();
            System.out.println("Active exception, redirecting to /confirm with email");
            response.sendRedirect("/confirm?email=" + email);
        } else {
            System.out.println("Another authentication exception");
            String mess = exception.getMessage();
            Optional<User> user = userRepository.findByEmail(mess);
            if(user.isPresent()){
                response.sendRedirect("/confirm?email=" + user.get().getEmail());
            }
            else{
                response.sendRedirect("/login?error");
            }
        }
    }
}