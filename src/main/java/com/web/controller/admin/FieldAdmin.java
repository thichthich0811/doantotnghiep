package com.web.controller.admin;

import com.web.entity.Field;
import com.web.entity.Products;
import com.web.repository.CategoryDAO;
import com.web.repository.FieldDAO;
import com.web.repository.ProductDAO;
import com.web.repository.SportTypeDAO;
import com.web.utils.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.List;

@Controller(value = "adminFieldController")
@RequestMapping("/admin")
public class FieldAdmin {

    @Autowired
    private FieldDAO fieldDAO;

    @Autowired
    private SportTypeDAO sportTypeDAO;

    @Autowired
    private CloudinaryService cloudinaryService;


    @RequestMapping(value = {"/fields"}, method = RequestMethod.GET)
    public String product(Model model, @RequestParam(required = false) Integer category) {
        if(category != null){
            if(category == -1) category = null;
        }
        List<Field> list = null;
        if(category == null){
            list = fieldDAO.findAll();
        }
        else{
            list = fieldDAO.findBySporttypeId(category);
        }
        model.addAttribute("listField", list);
        model.addAttribute("category", category);
        model.addAttribute("listCategory", sportTypeDAO.findAll());
        return "admin/fields";
    }

    @RequestMapping(value = {"/addfield"}, method = RequestMethod.GET)
    public String addField(Model model,@RequestParam(required = false) Integer id) {
        if(id == null){
            model.addAttribute("field", new Field());
            model.addAttribute("type", "add");
        }
        else{
            model.addAttribute("field", fieldDAO.findById(id).get());
            model.addAttribute("type", "update");
        }
        model.addAttribute("danhMucList", sportTypeDAO.findAll());
        return "admin/addfield";
    }

    @PostMapping("/add-field")
    public String postField(@ModelAttribute Field field, @RequestParam("imgbanner") MultipartFile imgbanner,
                              RedirectAttributes redirectAttributes) {
        String img = "";
        if(field.getId() != null){
            Field ex = fieldDAO.findById(field.getId()).get();
            img = ex.getImage();
        }
        if(!imgbanner.isEmpty()){
            img = cloudinaryService.uploadFile(imgbanner);
        }
        field.setImage(img);
        fieldDAO.save(field);
        redirectAttributes.addFlashAttribute("success", "Upload sân thành công!");
        return "redirect:/admin/fields";
    }


    @GetMapping("/delete-field")
    public String deleteField(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes){
        try {
            fieldDAO.deleteById(id);
        }
        catch (Exception e){
            Field p = fieldDAO.findById(id).get();
            p.setStatus(false);
            fieldDAO.save(p);
        }
        redirectAttributes.addFlashAttribute("message", "Xóa sân thành công!");
        return "redirect:/admin/fields";
    }
}
