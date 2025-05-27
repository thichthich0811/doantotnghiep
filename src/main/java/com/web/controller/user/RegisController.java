package com.web.controller.user;

import com.web.entity.User;
import com.web.enums.Role;
import com.web.repository.UserRepository;
import com.web.utils.MailService;
import com.web.utils.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.Optional;

@Controller
public class RegisController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserUtils userUtils;

    @RequestMapping(value = {"/regis"}, method = RequestMethod.GET)
    public String addblog(Model model) {
        model.addAttribute("user", new User());
        return "user/regis";
    }

    @RequestMapping(value = {"/confirm"}, method = RequestMethod.GET)
    public String confirm(Model model) {
        return "user/confirm";
    }

    @PostMapping("/regis")
    public String regisUser( @ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        System.out.println(user.getEmail());
        if (bindingResult.hasErrors()) {
            // Trả lại trang form với lỗi
            return "user/regis";
        }
        Optional<User> ex = userRepository.findByEmail(user.getEmail());
        if(ex.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
            return "redirect:/regis";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActived(false);
        user.setActivationKey(userUtils.randomKey());
        user.setRole(Role.ROLE_USER);
        user.setCreatedDate(new Date(System.currentTimeMillis()));
        User result = userRepository.save(user);
        mailService.sendEmail(user.getEmail(), "Xác nhận tài khoản của bạn","Cảm ơn bạn đã tin tưởng và xử dụng dịch vụ của chúng tôi:<br>" +
                "Để kích hoạt tài khoản của bạn, hãy nhập mã xác nhận bên dưới để xác thực tài khoản của bạn<br><br>" +
                "<a style=\"background-color: #2f5fad; padding: 10px; color: #fff; font-size: 18px; font-weight: bold;\">"+user.getActivationKey()+"</a>",false, true);
        return "redirect:/confirm?email="+user.getEmail();
    }


    @PostMapping("/confirm")
    public String confirmPost(@RequestParam String email, @RequestParam String key, RedirectAttributes redirectAttributes) {
        Optional<User> user = userRepository.getUserByActivationKeyAndEmail(key, email);
        if (user.isPresent()) {
            User exist = user.get();
            exist.setActivationKey(null);
            exist.setActived(true);
            userRepository.save(exist);
        }
        if(user.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "Mã xác nhận không chính xác!");
            return "redirect:/confirm?email="+email;
        }
        return "redirect:/login";
    }
}
