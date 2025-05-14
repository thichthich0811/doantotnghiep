package com.web.controller.admin;

import com.web.entity.Contacts;
import com.web.repository.ContactDAO;
import com.web.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;


@Controller(value = "adminContact")
@RequestMapping("/admin")
public class ContactController {

    @Autowired
    ContactDAO contactDAO;

    @GetMapping("phanhoi")
    public String view(Model model, @RequestParam(required = false) Date from, @RequestParam(required = false) Date to) {
        if(from == null || to == null){
            from = Date.valueOf("2000-01-01");
            to = Date.valueOf("2200-01-01");
        }
        model.addAttribute("contacts", contactDAO.findAllByDate(from, to));
        return "admin/contact";
    }
}
