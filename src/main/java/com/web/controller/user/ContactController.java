package com.web.controller.user;

import com.web.entity.Contacts;
import com.web.repository.ContactDAO;
import com.web.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


@Controller(value = "userContact")
public class ContactController {

    @Autowired
    ContactDAO contactDAO;

    @Autowired
    private UserUtils userUtils;

    @GetMapping("contact")
    public String view(Model model, HttpServletRequest request) {
        model.addAttribute("contacts", new Contacts());
        return "user/contact";
    }

    @PostMapping("/submit-contact")
    public String processContactForm(RedirectAttributes redirectAttributes,
                                     @ModelAttribute Contacts contacts) {

//        List<String> listcontacted = contactDAO.contactedInDay();
//        if(listcontacted.contains(userlogin)) {
//            redirectAttributes.addFlashAttribute("message1", "Để hạn chế spam. Bạn chỉ có thể gửi phản hồi mới vào ngày tiếp theo.");
//            return "redirect:/sportify/contact";
//        }
        contacts.setDatecontact(new Date(System.currentTimeMillis()));
        contacts.setUser(userUtils.getUserWithAuthority());
        contactDAO.save(contacts);
        redirectAttributes.addFlashAttribute("message", "Cảm ơn bạn đã phản hồi");
        return "redirect:/contact";
    }
}
