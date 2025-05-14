package com.web.utils;

import com.web.entity.User;
import com.web.enums.Role;
import com.web.repository.UserRepository;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String password = "admin";
        String email = "admin@gmail.com";

//         Kiểm tra xem tài khoản đã tồn tại chưa
        if (!userRepository.findByEmail(email).isPresent()) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setActived(true);
            user.setFullName("ADMIN");
            user.setRole(Role.ROLE_ADMIN);
            userRepository.save(user);
        }
    }
}
