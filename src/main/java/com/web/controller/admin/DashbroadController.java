package com.web.controller.admin;

import com.web.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashbroadController {

    @Autowired
    ProductDAO productDAO;
    @Autowired
    BookingDAO bookingDAO;
    @Autowired
    FieldDAO fieldDAO;
    @Autowired
    UserRepository userDAO;
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    EventDAO eventDAO;
    @Autowired
    ContactDAO contactDAO;
    @Autowired
    BookingDetailDAO bookingDetailDAO;

    @GetMapping("/index")
    public String dashbroad(Model model,@RequestParam(required = false) Integer year) {
        model.addAttribute("totalProduct", productDAO.count());
        model.addAttribute("totalField", fieldDAO.count());
        model.addAttribute("totalUser", userDAO.count());
        model.addAttribute("countOrderInDate", orderDAO.countOrderInDate());
        model.addAttribute("countBookingInDate", bookingDAO.countBookingInDate());
        model.addAttribute("countFieldActiving", fieldDAO.countFieldActiving());
        model.addAttribute("countProductActive", productDAO.countProductActive());
        model.addAttribute("listContactToday", contactDAO.findAllByDate());
        model.addAttribute("top3SanDatNhieu", bookingDetailDAO.top3SanDatNhieu());
        model.addAttribute("top3SanPhamNhieu", productDAO.top3BanChay());

        if(year == null){
            year = LocalDate.now().getYear();
        }
        List<Double> list = new ArrayList<>();
        List<Double> listBooking = new ArrayList<>();
        for(int i=1; i< 13; i++){
            Double sum = orderDAO.calDt(i, year);
            if(sum == null){
                sum = 0D;
            }
            list.add(sum);

            Double sumBooking = bookingDAO.calDt(i, year);
            if(sumBooking == null){
                sumBooking = 0D;
            }
            listBooking.add(sumBooking);
        }
        model.addAttribute("orderPoints", list);
        model.addAttribute("bookingPoints", listBooking);
        return "admin/index";
    }
}
