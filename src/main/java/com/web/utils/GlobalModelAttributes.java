package com.web.utils;

import com.web.entity.User;
import com.web.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelAttributes {
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private CartRepository cartRepository;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("soLuongGhGlobal", 0);
        User user = userUtils.getUserWithAuthority();
        if (user != null){
            model.addAttribute("soLuongGhGlobal", cartRepository.soLuongGh(user.getId()));
            model.addAttribute("userlogged", userUtils.getUserWithAuthority());
        }
        else{
            model.addAttribute("soLuongGhGlobal", 0);
        }
    }
}
