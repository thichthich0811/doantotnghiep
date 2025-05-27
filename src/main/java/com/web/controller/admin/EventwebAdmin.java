package com.web.controller.admin;
import com.web.entity.Eventweb;
import com.web.repository.EventDAO;
import com.web.utils.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.sql.Date;
@Controller(value = "adminEventwebController")
@RequestMapping("/admin")
public class EventwebAdmin {
    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private CloudinaryService cloudinaryService;
    @RequestMapping(value = {"/events"}, method = RequestMethod.GET)
    public String events(Model model, @RequestParam(required = false) Date start
            , @RequestParam(required = false) Date end) {
        if(start == null || end == null){
            start = Date.valueOf("2000-01-01");
            end = Date.valueOf("2200-01-01");
        }
        model.addAttribute("listEvent", eventDAO.findByDate(start, end));
        return "admin/events";
    }

    @RequestMapping(value = {"/addevent"}, method = RequestMethod.GET)
    public String addevent(Model model, @RequestParam(required = false) Integer id) {
        model.addAttribute("event", new Eventweb());
        if(id != null){
            model.addAttribute("event", eventDAO.findById(id).get());
        }
        return "admin/addevent";
    }

    @PostMapping(value = {"/addevent"})
    public String add(@ModelAttribute Eventweb event, @RequestParam("imgbanner") MultipartFile file, RedirectAttributes redirectAttributes) {
        if(event.getId() != null){
            Eventweb ex = eventDAO.findById(event.getId()).get();
            event.setImage(ex.getImage());
        }
        if (!file.isEmpty()) {
            String img = cloudinaryService.uploadFile(file);
            event.setImage(img);
        }
        eventDAO.save(event);
        redirectAttributes.addFlashAttribute("message", "Thêm sự kiện thành công!");
        return "redirect:/admin/events";
    }

    @GetMapping(value = {"/delete-event"})
    public String delete(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        eventDAO.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Xóa sự kiện thành công!");
        return "redirect:/admin/events";
    }
}
