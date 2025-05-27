package com.web.controller.admin;

import com.web.entity.User;
import com.web.enums.Role;
import com.web.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.sql.Date;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class TaiKhoanAdmin {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @RequestMapping(value = {"/tai-khoan"}, method = RequestMethod.GET)
    public String danhMucGet(Model model, @RequestParam(value = "role", required = false) Role role) {
        if(role == null){
            model.addAttribute("taiKhoanList", userRepository.findAll());
        }
        else{
            model.addAttribute("taiKhoanList", userRepository.findByRole(role));
        }
        model.addAttribute("roleSelect",role);
        model.addAttribute("user",new User());
        return "admin/taikhoan";
    }
    @PostMapping("/update-role")
    public String updateRole(@RequestParam Long idUser, @RequestParam Role role) {
        User user = userRepository.findById(idUser).get();
        user.setRole(role);
        userRepository.save(user);
        return "redirect:tai-khoan?update-role-success=true";
    }
    @PostMapping("/lockOrUnlockUser")
    public String deleteCategory(RedirectAttributes redirectAttributes, HttpServletRequest request, @RequestParam("id") Long id){
        User user = userRepository.findById(id).get();
        String mess = "";
        if(user.getActived() == true){
            user.setActived(false);
            userRepository.save(user);
            mess = "Khóa tài khoản thành công";
        }
        else{
            user.setActived(true);
            userRepository.save(user);
            mess = "Mở khóa tài khoản thành công";
        }
        redirectAttributes.addFlashAttribute("success", mess);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
    @PostMapping("/add-account")
    public String regisUser(@ModelAttribute User user, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        System.out.println(user.getEmail());
        Optional<User> ex = userRepository.findByEmail(user.getEmail());
        String referer = request.getHeader("Referer");
        if(ex.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
            return "redirect:" + referer;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActived(true);
        user.setCreatedDate(new Date(System.currentTimeMillis()));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success","");
        return "redirect:" + referer;
    }

    @GetMapping("/delete-account")
    public String deleteAcc(RedirectAttributes redirectAttributes, HttpServletRequest request, @RequestParam("id") Long id){
        try {
            userRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa tài khoản thành công");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản đã có liên kết, không thể xóa");
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}
