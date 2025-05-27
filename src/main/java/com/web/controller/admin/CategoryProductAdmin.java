package com.web.controller.admin;
import com.web.entity.Categories;
import com.web.repository.CategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequestMapping("/admin")
public class CategoryProductAdmin {
    @Autowired
    private CategoryDAO categoryDAO;
    @RequestMapping(value = {"/danhmuc"}, method = RequestMethod.GET)
    public String danhmuc(Model model) {
        model.addAttribute("listcate", categoryDAO.findAll());
        return "admin/category-product";
    }
    @PostMapping(value = {"/addcategory"})
    public String addcategory(@RequestParam String name, RedirectAttributes redirectAttributes) {
        Categories category = new Categories();
        category.setCategoryName(name);
        categoryDAO.save(category);
        redirectAttributes.addFlashAttribute("message", "Thêm danh mục thành công!");
        return "redirect:/admin/danhmuc";
    }
    @PostMapping(value = {"/updatecategory"})
    public String updatecategory(@RequestParam String name, @RequestParam Integer id, RedirectAttributes redirectAttributes) {
        Categories category = categoryDAO.findById(id).get();
        category.setCategoryName(name);
        categoryDAO.save(category);
        redirectAttributes.addFlashAttribute("message", "Cập nhật danh mục thành công!");
        return "redirect:/admin/danhmuc";
    }
    @GetMapping(value = {"/deletecategory"})
    public String deletecategory(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            categoryDAO.deleteById(id);
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Danh mục đã có liên kết, không thể xóa!");
        }
        redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công!");
        return "redirect:/admin/danhmuc";
    }
}
