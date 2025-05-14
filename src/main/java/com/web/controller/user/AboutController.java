package com.web.controller.user;

import com.web.repository.EventDAO;
import com.web.repository.FieldDAO;
import com.web.repository.ProductDAO;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller(value = "about")
public class AboutController {
    @Autowired
    UserRepository userDAO;
    @Autowired
    FieldDAO fieldDAO;
    @Autowired
    ProductDAO productDAO;
    @Autowired
    EventDAO eventDAO;
    @GetMapping("/about")
    public String about(Model model){
        Long userCount = userDAO.count();
        List<Object> fieldCount = fieldDAO.CountField();
        List<Object> eventCount = eventDAO.CountEvent();
        Long productCount = productDAO.count();

        model.addAttribute("userCount", userCount);
        model.addAttribute("fieldCount", fieldCount);
        model.addAttribute("eventCount", eventCount);
        model.addAttribute("productCount", productCount);
        return "user/about";
    }

    @GetMapping("chinhsach")
    public String viewchinhsach(Model model) {
        return"user/chinhsach";
    }

    @GetMapping("quydinh")
    public String viewdieukien(Model model) {
        return "user/quydinh";
    }
}
