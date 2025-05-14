package com.web.controller.admin;

import com.web.entity.Orderdetails;
import com.web.entity.Orders;
import com.web.enums.Paytype;
import com.web.repository.OrderDAO;
import com.web.repository.ProductDAO;
import com.web.utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Controller(value = "adminInvoiceController")
@RequestMapping("/admin")
public class InvoiceController {

    @Autowired
    private OrderDAO invoiceRepository;

    @Autowired
    private ProductDAO productDAO;

    @RequestMapping(value = {"/invoice"}, method = RequestMethod.GET)
    public String invoice(Model model, @RequestParam(required = false) String fromDate,
                          @RequestParam(required = false) String toDate,
                          @RequestParam(required = false) String trangThaiDonHang,
                          @RequestParam(required = false) String trangThaiThanhToanStr) {
        Date from = null;
        Date to = null;
        Boolean trangThaiThanhToan = null;
        if ("true".equals(trangThaiThanhToanStr)) {
            trangThaiThanhToan = true;
        } else if ("false".equals(trangThaiThanhToanStr)) {
            trangThaiThanhToan = false;
        }
        if(trangThaiDonHang != null && trangThaiDonHang.equals("")){
            trangThaiDonHang = null;
        }
        if(fromDate != null && toDate != null){
            if(!fromDate.equals("") && !toDate.equals("")){
                from = Date.valueOf(fromDate);
                to = Date.valueOf(toDate);
            }
        }
        if(from == null || to == null){
            from = Date.valueOf("2000-01-01");
            to = Date.valueOf("2200-01-01");
        }
        List<Orders> list = null;
        if(trangThaiDonHang == null && trangThaiThanhToan == null){
            list = invoiceRepository.findByDate(from, to);
        }
        if(trangThaiDonHang == null && trangThaiThanhToan != null){
            list = invoiceRepository.findByDateAndThanhToan(from, to, trangThaiThanhToan);
        }
        if(trangThaiDonHang != null && trangThaiThanhToan == null){
            list = invoiceRepository.findByDateAndTrangThaiDonHang(from, to,trangThaiDonHang);
        }
        if(trangThaiDonHang != null && trangThaiThanhToan != null){
            list = invoiceRepository.findByAll(from, to,trangThaiDonHang,trangThaiThanhToan);
        }
        model.addAttribute("listInvoice", list);
        return "admin/invoice";
    }

    @PostMapping("/update-status-invoice")
    public String updateStatus(@RequestParam Integer invoiceId, @RequestParam String statusName, RedirectAttributes redirectAttributes) {
        Orders orders = invoiceRepository.findById(invoiceId).get();
        if(orders.getOrderStatus().equals(OrderStatus.DA_HUY) || orders.getOrderStatus().equals(OrderStatus.DA_NHAN)){
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không thể cập nhật trạng thái!");
            return "redirect:/admin/invoice";
        }
        if(statusName.equals(OrderStatus.DA_NHAN)){
            orders.setPaymentStatus(true);
        }
        orders.setOrderStatus(statusName);
        invoiceRepository.save(orders);
        if(statusName.equals(OrderStatus.DA_HUY)){
            List<Orderdetails> orderdetails = orders.getOrderDetails();
            for (Orderdetails d: orderdetails) {
                d.getProducts().setQuantity(d.getProducts().getQuantity() + d.getQuantity());
                productDAO.save(d.getProducts());
            }
        }
        redirectAttributes.addFlashAttribute("message", "Đã cập nhật trạng thái '"+statusName+"'"+" cho đơn hàng "+invoiceId);
        return "redirect:/admin/invoice";
    }
}
