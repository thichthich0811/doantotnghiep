package com.web.controller.admin;

import com.web.entity.Bookings;
import com.web.entity.Orderdetails;
import com.web.entity.Orders;
import com.web.repository.BookingDAO;
import com.web.repository.OrderDAO;
import com.web.utils.BookingStatus;
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

@Controller(value = "adminBookingController")
@RequestMapping("/admin")
public class BookingController {

    @Autowired
    private BookingDAO bookingDAO;

    @RequestMapping(value = {"/booking"}, method = RequestMethod.GET)
    public String invoice(Model model, @RequestParam(required = false) String fromDate,
                          @RequestParam(required = false) String toDate,
                          @RequestParam(required = false) String trangThaiThanhToan) {
        Date from = null;
        Date to = null;
        if(trangThaiThanhToan != null && trangThaiThanhToan.equals("")){
            trangThaiThanhToan = null;
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
        List<Bookings> list = null;
        if(trangThaiThanhToan == null){
            list = bookingDAO.findByDate(from, to);
        }
        else{
            list = bookingDAO.findByDateAndTrangThai(from, to, trangThaiThanhToan);
        }
        model.addAttribute("listBooking", list);
        return "admin/booking";
    }

    @PostMapping("/xac-nhan-thanh-toan-booking")
    public String updateStatus(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        Bookings bookings = bookingDAO.findById(id).get();
        bookings.setBookingStatus(BookingStatus.DA_THANH_TOAN);
        bookingDAO.save(bookings);
        redirectAttributes.addFlashAttribute("message", "Đã cập nhật trạng thái '"+BookingStatus.DA_THANH_TOAN+"'"+" cho đơn đặt "+id);
        return "redirect:/admin/booking";
    }
}
