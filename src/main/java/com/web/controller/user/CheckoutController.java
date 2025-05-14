package com.web.controller.user;
import com.web.entity.*;
import com.web.enums.Paytype;
import com.web.repository.*;
import com.web.utils.OrderStatus;
import com.web.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = {"/checkout"})
public class CheckoutController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductDAO productRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @Autowired
    private VoucherDAO voucherDAO;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = {"/order"}, method = RequestMethod.POST)
    public String checkoutPost(RedirectAttributes redirectAttributes, HttpServletRequest request,
                               @RequestParam String fullName, @RequestParam String phone, @RequestParam String address,
                               @RequestParam String note, @RequestParam String voucher) {
        List<Cart> list = cartRepository.findByUser(userUtils.getUserWithAuthority().getId());
        Double d = 0D;
        for(Cart g : list){
            d += g.getQuantity() * g.getProducts().getPrice();
            if(g.getQuantity() > g.getProducts().getQuantity()){
                redirectAttributes.addFlashAttribute("warning", "Số lượng sản phẩm "+g.getProducts().getProductName()+" không được vượt quá: "+g.getProducts().getQuantity());
                String referer = request.getHeader("Referer");
                return "redirect:" + referer;
            }
        }
        if(!voucher.equals("-1")){
            Voucher v = voucherDAO.findByCode(voucher).get();
            d = d - (d * v.getDiscountPercent() / 100);
        }
        User user = userUtils.getUserWithAuthority();
        Orders orders = new Orders();
        orders.setOrderStatus(OrderStatus.DANG_CHO);
        orders.setUser(user);
        orders.setAddress(address);
        orders.setCreateDate(new Date(System.currentTimeMillis()));
        orders.setNote(note);
        orders.setPaymentStatus(false);
        orders.setTotalPrice(d);
        orders.setFullName(fullName);
        orders.setPhone(phone);
        orderDAO.save(orders);
        for(Cart g : list){
            Orderdetails od = new Orderdetails();
            od.setProducts(g.getProducts());
            od.setQuantity(g.getQuantity());
            od.setOrders(orders);
            od.setPrice(g.getProducts().getPrice());
            orderDetailDAO.save(od);
            g.getProducts().setQuantity(g.getProducts().getQuantity() - g.getQuantity());
            productRepository.save(g.getProducts());
        }
        if(user.getAddress() == null){
            user.setAddress(address);
            userRepository.save(user);
        }
        redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
        return "redirect:/cart";
    }
}
