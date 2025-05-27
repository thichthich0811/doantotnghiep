package com.web.controller.user;

import com.web.entity.Contacts;
import com.web.repository.ContactDAO;
import com.web.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.sql.Date;

@Controller(value = "userContact")
public class ContactController {
    @Autowired
    private ContactDAO contactDAO;
    @Autowired
    private UserUtils userUtils;

    @GetMapping("contact")
    public String view(Model model, HttpServletRequest request) {
        model.addAttribute("contacts", new Contacts());
        return "user/contact";
    }

    @PostMapping("/submit-contact")
    public String processContactForm(RedirectAttributes redirectAttributes, @ModelAttribute Contacts contacts) {
        contacts.setDatecontact(new Date(System.currentTimeMillis()));
        contacts.setUser(userUtils.getUserWithAuthority());
        contactDAO.save(contacts);
        redirectAttributes.addFlashAttribute("message", "Cảm ơn bạn đã phản hồi");
        return "redirect:/contact";
    }
}
