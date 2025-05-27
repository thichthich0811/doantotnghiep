package com.web.controller.user;
import com.web.entity.Field;
import com.web.entity.Products;
import com.web.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private FieldDAO fieldDAO;
    @Autowired
    private BookingDetailDAO bookingDetailDAO;
    @Autowired
    private ProductDAO productDAO;
    private Integer size = 8;
    @GetMapping(value = {"/","/index"})
    public String view(Model model, HttpServletRequest request, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        Page<Products> page = productDAO.findAll(pageable);
        model.addAttribute("sanPhamList", page.getContent());
        model.addAttribute("tongSoTrang", page.getTotalPages());
        model.addAttribute("pageable", pageable);
//        List<Object[]> eventList = eventDAO.fillEventInMonth();
//        model.addAttribute("eventList", eventList);
        List<Field> fieldList = fieldDAO.findAll();
        model.addAttribute("fieldList", fieldList);
//      model.addAttribute("lastBlog", eventDAO.lastEvent());
//      model.addAttribute("listBlog", eventDAO.getEventIndex());
        return "user/index";
    }

}
