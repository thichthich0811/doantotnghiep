package com.web.controller.user;

import com.web.dto.ProductSpecification;
import com.web.entity.Eventweb;
import com.web.entity.Products;
import com.web.repository.CategoryDAO;
import com.web.repository.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class productController {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    Integer size = 8;

    @GetMapping("/chitietsanpham")
    public String detail(Model model, @RequestParam Integer id, Authentication authentication) {
        model.addAttribute("isAuthenticated", authentication != null && authentication.isAuthenticated());
        Products product = productDAO.findById(id).get();
        model.addAttribute("product", product);
        return "user/product-single";
    };

    @RequestMapping(value = {"/product"}, method = RequestMethod.GET)
    public String product(@RequestParam(required = false) Double smallPrice, @RequestParam(required = false) Double largePrice,
                          @RequestParam(required = false) List<Long> category, Model model, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        ProductSpecification productSpecification = new ProductSpecification(category, smallPrice, largePrice);
        Page<Products> page = productDAO.findAll(productSpecification, pageable);
        model.addAttribute("sanPhamList", page.getContent());
        model.addAttribute("tongSoTrang", page.getTotalPages());
        model.addAttribute("pageable", pageable);
        model.addAttribute("danhMuc", categoryDAO.findAll());
        if(category != null){
            model.addAttribute("categories", category);
        }
        if(smallPrice != null && largePrice != null){
            model.addAttribute("smallPrice", smallPrice);
            model.addAttribute("largePrice", largePrice);
        }
        return "user/product";
    }
}
