package com.web.controller.user;

import com.web.entity.User;
import com.web.repository.UserRepository;
import com.web.utils.MailService;
import com.web.utils.UserUtils;
import com.web.validate.RegisValidate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ForgotPasswordController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegisValidate regisValidate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserUtils userUtils;
    @RequestMapping(value = {"/forgot"}, method = RequestMethod.GET)
    public String forgot(Model model) {
        return "user/forgot";
    }
    @RequestMapping(value = {"/datlaimatkhau"}, method = RequestMethod.GET)
    public String datlaimatkhau(Model model,@RequestParam String email, @RequestParam String key) {
        model.addAttribute("email",email);
        model.addAttribute("key",key);
        return "user/datlaimatkhau";
    }

    @PostMapping("/forgot")
    public String forgotPost(@RequestParam String email, RedirectAttributes redirectAttributes) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent() == false){
            redirectAttributes.addFlashAttribute("error","Không tìm thấy tài khoản");
            return "redirect:/forgot";
        }
        else if(user.get().getActivationKey() != null && user.get().getActived() == false){
            redirectAttributes.addFlashAttribute("error","Tài khoản chưa được kích hoạt");
            return "redirect:/forgot";
        }
        else if(user.get().getActived() == false && user.get().getActivationKey() == null){
            redirectAttributes.addFlashAttribute("error","Tài khoản đã bị khóa");
            return "redirect:/forgot";
        }
        String random = UUID.randomUUID().toString();
        user.get().setRememberKey(random);
        userRepository.save(user.get());

        mailService.sendEmail(email, "Đặt lại mật khẩu","Cảm ơn bạn đã tin tưởng và xử dụng dịch vụ của chúng tôi:<br>" +
                "Chúng tôi đã tạo một mật khẩu mới từ yêu cầu của bạn<br>" +
                "Hãy lick vào bên dưới để đặt lại mật khẩu mới của bạn<br><br>" +
                "<a href='http://localhost:8080/datlaimatkhau?email="+email+"&key="+random+"' style=\"background-color: #2f5fad; padding: 10px; color: #fff; font-size: 18px; font-weight: bold;\">Đặt lại mật khẩu</a>",false, true);
        redirectAttributes.addFlashAttribute("success","");
        return "redirect:/forgot";
    }
    @PostMapping("/datlaimatkhau")
    public String datLaiMatKhau(@RequestParam String email, @RequestParam String key, HttpServletRequest request,
                                @RequestParam String password, RedirectAttributes redirectAttributes) throws URISyntaxException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.get().getRememberKey().equals(key)){
            user.get().setPassword(passwordEncoder.encode(password));
            userRepository.save(user.get());
        }
        else{
            redirectAttributes.addFlashAttribute("error","Mã xác thực không chính xác");
        }
        redirectAttributes.addFlashAttribute("success","");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
