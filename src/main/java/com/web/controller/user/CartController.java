package com.web.controller.user;

import com.web.entity.Cart;
import com.web.entity.Products;
import com.web.entity.User;
import com.web.repository.CartRepository;
import com.web.repository.ProductDAO;
import com.web.repository.VoucherDAO;
import com.web.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;

@Controller
public class CartController {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductDAO productRepository;
    @Autowired
    private VoucherDAO voucherDAO;
    @Autowired
    private UserUtils userUtils;
    @RequestMapping(value = {"/cart"}, method = RequestMethod.GET)
    public String gioHang(Model model) {
        List<Cart> list = cartRepository.findByUser(userUtils.getUserWithAuthority().getId());
        Double d = 0D;
        for(Cart g : list){
            d += g.getQuantity() * (g.getProducts().getPrice()-g.getProducts().getDiscountPrice());
        }
        model.addAttribute("gioHangList",list);
        model.addAttribute("tongTien",d);
        model.addAttribute("vouchers",voucherDAO.fillActive());
        return "user/cart";
    }

    @PostMapping("/add-cart")
    public String add(RedirectAttributes redirectAttributes, HttpServletRequest request,
                      @RequestParam Integer soLuong, @RequestParam Integer id){
        User user = userUtils.getUserWithAuthority();
        Optional<Cart> gioHang = cartRepository.findByUserAndIdSp(user.getId(), id);
        Products sanPham = productRepository.findById(id).get();
        if(gioHang.isPresent()){
            redirectAttributes.addFlashAttribute("message", "Thêm sản phẩm vào giỏ hàng thành công!");
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
        Cart gh = new Cart();
        gh.setQuantity(soLuong);
        gh.setProducts(sanPham);
        gh.setUser(user);
        cartRepository.save(gh);
        redirectAttributes.addFlashAttribute("message", "Thêm sản phẩm vào giỏ hàng thành công!");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/update-sl")
    public String upDateSl(RedirectAttributes redirectAttributes, HttpServletRequest request,
                           @RequestParam Integer soLuong, @RequestParam Long id){
        Cart gioHang = cartRepository.findById(id).get();
        gioHang.setQuantity(gioHang.getQuantity() + soLuong);
        if(gioHang.getQuantity() == 0){
            cartRepository.delete(gioHang);
        }
        else {
            cartRepository.save(gioHang);
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/delete-giohang")
    public String deleteDonHang(RedirectAttributes redirectAttributes, @RequestParam("id") Long id){
        cartRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa giỏ hàng thành công!");
        return "redirect:cart";
    }
    
}

