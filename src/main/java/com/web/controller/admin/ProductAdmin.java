package com.web.controller.admin;
import com.web.entity.Products;
import com.web.repository.CategoryDAO;
import com.web.repository.ProductDAO;
import com.web.utils.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.List;

@Controller(value = "adminProductController")
@RequestMapping("/admin")
public class ProductAdmin {
    @Autowired
    private ProductDAO productRepository;
    @Autowired
    private CategoryDAO categoryRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @RequestMapping(value = {"/product"}, method = RequestMethod.GET)
    public String product(Model model, @RequestParam(required = false) Long category) {
        if(category != null){
            if(category == -1) category = null;
        }
        List<Products> list = null;
        if(category == null){
            list = productRepository.findAll();
        }
        else{
            list = productRepository.findByCategory(category);
        }
        model.addAttribute("listProduct", list);
        model.addAttribute("category", category);
        model.addAttribute("listCategory", categoryRepository.findAll());
        return "admin/product";
    }
    @RequestMapping(value = {"/addproduct"}, method = RequestMethod.GET)
    public String addproduct(Model model,@RequestParam(required = false) Integer id) {
        if(id == null){
            model.addAttribute("product", new Products());
            model.addAttribute("type", "add");
        }
        else{
            model.addAttribute("product", productRepository.findById(id).get());
            model.addAttribute("type", "update");
        }
        model.addAttribute("danhMucList", categoryRepository.findAll());
        return "admin/addproduct";
    }

    @PostMapping("/add-san-pham")
    public String postSanPham(@ModelAttribute Products product, @RequestParam("imgbanner") MultipartFile imgbanner,
                              RedirectAttributes redirectAttributes) {
        String img = "";
        product.setDateCreate(new Date(System.currentTimeMillis()));
        if(product.getId() != null){
            Products ex = productRepository.findById(product.getId()).get();
            product.setDateCreate(ex.getDateCreate());
            product.setQuantitySold(ex.getQuantitySold());
            img = ex.getImage();
        }
        if(!imgbanner.isEmpty()){
            img = cloudinaryService.uploadFile(imgbanner);
            product.setQuantitySold(0);
        }
        product.setImage(img);
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("success", "Upload sản phẩm thành công!");
        return "redirect:/admin/product";
    }


    @GetMapping("/delete-sanpham")
    public String deleteSanPham(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes){
        try {
            productRepository.deleteById(id);
        }
        catch (Exception e){
            Products p = productRepository.findById(id).get();
            p.setProductStatus(false);
            productRepository.save(p);
        }
        redirectAttributes.addFlashAttribute("message", "Xóa sản phẩm thành công!");
        return "redirect:/admin/product";
    }
}
