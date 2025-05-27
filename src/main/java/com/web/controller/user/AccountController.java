package com.web.controller.user;

import com.web.entity.User;
import com.web.repository.BookingDAO;
import com.web.repository.OrderDAO;
import com.web.repository.UserRepository;
import com.web.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller(value = "taiKhoanUserController")
public class AccountController {
    @Autowired
    private OrderDAO invoiceRepository;
    @Autowired
    private BookingDAO bookingDAO;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = {"/taikhoan"}, method = RequestMethod.GET)
    public String taikhoan(Model model) {
        model.addAttribute("donHangList",invoiceRepository.findByUser(userUtils.getUserWithAuthority().getId()));
        model.addAttribute("bookingList",bookingDAO.findByUser(userUtils.getUserWithAuthority().getId()));
        model.addAttribute("userlogged",userUtils.getUserWithAuthority());
        return "user/account";
    }

    @RequestMapping(value = {"/update-infor"}, method = RequestMethod.POST)
    public String updateInfor(RedirectAttributes redirectAttributes, HttpServletRequest request,
                              @RequestParam String fullName, @RequestParam String phone, @RequestParam String address) {
        User user = userUtils.getUserWithAuthority();
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAddress(address);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công!");
        return "redirect:/taikhoan#infor" ;
    }

    @RequestMapping(value = {"/update-password"}, method = RequestMethod.POST)
    public String updatePassword(RedirectAttributes redirectAttributes, HttpServletRequest request,
                                 @RequestParam String oldpass, @RequestParam String newpass) {
        User user = userUtils.getUserWithAuthority();
        if(passwordEncoder.matches(oldpass, user.getPassword())){
            user.setPassword(passwordEncoder.encode(newpass));
            userRepository.save(user);
        }
        else{
            redirectAttributes.addFlashAttribute("error", "Mât khẩu cũ không chính xác");
            return "redirect:/taikhoan#changepass" ;
        }
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", "Cập nhật mật khẩu thành công!");
        return "redirect:/taikhoan#changepass" ;
    }
}
