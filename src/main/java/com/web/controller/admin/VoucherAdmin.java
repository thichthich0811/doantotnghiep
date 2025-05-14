package com.web.controller.admin;

import com.web.entity.Field;
import com.web.entity.Sporttype;
import com.web.entity.Voucher;
import com.web.repository.SportTypeDAO;
import com.web.repository.VoucherDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class VoucherAdmin {

    @Autowired
    private VoucherDAO voucherDAO;

    @RequestMapping(value = {"/voucher"}, method = RequestMethod.GET)
    public String vouchers(Model model, @RequestParam(required = false) String loai) {
        List<Voucher> list = voucherDAO.findAll();
        if(loai != null && loai != "-1"){
            if(loai.equals("con-han")){
                list = voucherDAO.fillActive();
            }
            if(loai.equals("het-han")){
                list = voucherDAO.fillInActive();
            }
            if(loai.equals("chua-ap-dung")){
                list = voucherDAO.fillWillActive();
            }
        }
        model.addAttribute("listvoucher", list);
        return "admin/voucher";
    }

    @RequestMapping(value = {"/addvoucher"}, method = RequestMethod.GET)
    public String addField(Model model,@RequestParam(required = false) Integer id) {
        if(id == null){
            model.addAttribute("voucher", new Voucher());
            model.addAttribute("type", "add");
        }
        else{
            model.addAttribute("voucher", voucherDAO.findById(id).get());
            model.addAttribute("type", "update");
        }
        return "admin/addvoucher";
    }

    @PostMapping("/add-voucher")
    public String postField(@ModelAttribute Voucher voucher, RedirectAttributes redirectAttributes) {
        if(voucher.getId() != null){
            Optional<Voucher> v = voucherDAO.findByCodeAndId(voucher.getCode(), voucher.getId());
            if(v.isPresent()){
                redirectAttributes.addFlashAttribute("error", "Mã voucher đã tồn tại!");
                return "redirect:/admin/addvoucher";
            }
        }
        else{
            Optional<Voucher> v = voucherDAO.findByCode(voucher.getCode());
            if(v.isPresent()){
                redirectAttributes.addFlashAttribute("error", "Mã voucher đã tồn tại!");
                return "redirect:/admin/addvoucher";
            }
        }
        voucherDAO.save(voucher);
        redirectAttributes.addFlashAttribute("success", "Thêm voucher thành công!");
        return "redirect:/admin/voucher";
    }


    @GetMapping("/delete-voucher")
    public String deleteField(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes){
        try {
            voucherDAO.deleteById(id);
        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Không thể xóa voucher này!");
            return "redirect:/admin/voucher";
        }
        redirectAttributes.addFlashAttribute("message", "Xóa voucher thành công!");
        return "redirect:/admin/voucher";
    }

}
