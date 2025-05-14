package com.web.utils;

import com.web.entity.User;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    @Autowired
    private UserRepository userRepository;

    public User getUserWithAuthority() {
        // Lấy Authentication từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            System.out.println("username: "+username);
            if(username.equalsIgnoreCase("anonymousUser")){
                return null;
            }
            User user = userRepository.findByEmail(username).get();
            return user;
        }
        return null;
    }

    public String randomKey(){
        String str = "12345667890";
        Integer length = str.length()-1;
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i=0; i<6; i++){
            Integer ran = (int)(Math.random()*length);
            stringBuilder.append(str.charAt(ran));
        }
        return String.valueOf(stringBuilder);
    }

//    public String randomPass(){
//        String str = "qwert1yui2op3as4dfg5hj6klzx7cvb8nmQ9WE0RTYUIOPASDFGHJKLZXCVBNM";
//        Integer length = str.length()-1;
//        StringBuilder stringBuilder = new StringBuilder("");
//        for(int i=0; i<7; i++){
//            Integer ran = (int)(Math.random()*length);
//            stringBuilder.append(str.charAt(ran));
//        }
//        return String.valueOf(stringBuilder);
//    }
}
