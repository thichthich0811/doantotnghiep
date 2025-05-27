package com.web.controller.user;

import com.web.entity.Eventweb;
import com.web.repository.EventDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller(value = "userInstructController")
public class EventController {
    @Autowired
    private EventDAO eventDAO;
    private Integer size = 8;
    @RequestMapping(value = {"/event"}, method = RequestMethod.GET)
    public String blog(Model model, @RequestParam(required = false) String search, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        if(search == null) search = "";
        search = "%"+search+"%";
        Page<Eventweb> page = eventDAO.findByParam(search,pageable);
        model.addAttribute("blogList", page.getContent());
        model.addAttribute("tongSoTrang", page.getTotalPages());
        model.addAttribute("pageable", pageable);
        return "user/blog";
    }
    @RequestMapping(value = {"/event-detail"}, method = RequestMethod.GET)
    public String chiTietBlog(Model model, @RequestParam Integer id) {
        model.addAttribute("event", eventDAO.findById(id).get());
        return "user/blog-single";
    }
}
