package com.web.controller.admin;

import com.web.entity.Categories;
import com.web.entity.Sporttype;
import com.web.repository.CategoryDAO;
import com.web.repository.SportTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class SporttypeController {

    @Autowired
    private SportTypeDAO sportTypeDAO;

    @RequestMapping(value = {"/danhmuc-thethao"}, method = RequestMethod.GET)
    public String danhmuc(Model model) {
        model.addAttribute("listcate", sportTypeDAO.findAll());
        return "admin/category-sport";
    }

    @PostMapping(value = {"/add-danhmuc-thethao"})
    public String addcategory(@RequestParam String name, RedirectAttributes redirectAttributes) {
        Sporttype category = new Sporttype();
        category.setCategoryName(name);
        sportTypeDAO.save(category);
        redirectAttributes.addFlashAttribute("message", "Thêm danh mục thành công!");
        return "redirect:/admin/danhmuc-thethao";
    }

    @PostMapping(value = {"/update-danhmuc-thethao"})
    public String updatecategory(@RequestParam String name, @RequestParam Integer id, RedirectAttributes redirectAttributes) {
        Sporttype category = sportTypeDAO.findById(id).get();
        category.setCategoryName(name);
        sportTypeDAO.save(category);
        redirectAttributes.addFlashAttribute("message", "Cập nhật danh mục thành công!");
        return "redirect:/admin/danhmuc-thethao";
    }

    @GetMapping(value = {"/delete-danhmuc-thethao"})
    public String deletecategory(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            sportTypeDAO.deleteById(id);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Danh mục đã có liên kết, không thể xóa!");
        }
        redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công!");
        return "redirect:/admin/danhmuc-thethao";
    }
}
