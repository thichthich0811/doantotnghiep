package com.web.controller.user;
import java.util.ArrayList;
import java.util.List;

import com.web.entity.*;
import com.web.repository.SportTypeDAO;
import com.web.repository.TeamDAO;
import com.web.repository.TeamDetailDAO;
import com.web.repository.UserRepository;
import com.web.service.SportTypeService;
import com.web.service.TeamDetailService;
import com.web.service.TeamService;
import com.web.utils.CloudinaryService;
import com.web.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("unused")
@Controller
public class TeamController {

    @Autowired
    private TeamDAO teamdao;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserUtils userUtils;

    @Autowired
    TeamDetailDAO detailDAO;

    @Autowired
    TeamDetailService teamDetailService;

    @Autowired
    TeamService teamService;

    @Autowired
    SportTypeService sportTypeService;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    SportTypeDAO sportTypeDAO;

    Integer size = 4;

    @ModelAttribute("sporttypeList")
    public List<Sporttype> getSporttypeList() {
        return sportTypeDAO.findAll();
    }

    // Đỗ toàn bộ dữ liệu liên quan đến team
    @GetMapping("/team")
    public String viewTeam(Model model, HttpServletRequest request, Pageable pageable,
                           @RequestParam(required = false) String searchText,
                           @RequestParam(required = false) Integer sporttypeid) {
        pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        if(searchText != null){
            searchText = "%"+searchText+"%";
        }
        Page<Teams> teamsPage = null;
        if(searchText != null){
            teamsPage = teamdao.findByParam(searchText, pageable);
        }
        else if(sporttypeid != null && sporttypeid != -1){
            teamsPage = teamdao.findBySporttype(sporttypeid, pageable);
        }
        else{
            teamsPage = teamdao.findAll(pageable);
        }
        List<Teams> teamsList = teamsPage.getContent();
        User user = userUtils.getUserWithAuthority();
        if(user != null){
            for(Teams t : teamsList){
                Boolean checkTonTai = false;
                for(Teamdetails td : t.getTeamdetails()){
                    if(td.getUser().getId() == user.getId() && td.getStatus() == true){
                        checkTonTai = true;
                        break;
                    }
                }
                if(checkTonTai == true){
                    t.setDaThamGia(true);
                }
            }
        }
        model.addAttribute("teamList", teamsList);
        model.addAttribute("tongSoTrang", teamsPage.getTotalPages());
        model.addAttribute("pageable", pageable);
        model.addAttribute("team", new Teams());
        return "user/doi";
    }

    @PostMapping(value = {"/team/createTeam"})
    public String add(@ModelAttribute Teams team, @RequestParam("newAvatar") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (!file.isEmpty()) {
            String img = cloudinaryService.uploadFile(file);
            team.setAvatar(img);
        }
        User u = userUtils.getUserWithAuthority();
        team.setCreateDate(LocalDate.now());
        team.setUser(u);
        team.setUsername(u.getEmail());
        teamdao.save(team);

        Teamdetails teamdetails = new Teamdetails();
        teamdetails.setTeams(team);
        teamdetails.setUser(u);
        teamdetails.setJoinDate(LocalDate.now());
        teamdetails.setStatus(false);
        detailDAO.save(teamdetails);
        redirectAttributes.addFlashAttribute("message", "Tạo đội thành công!");
        return "redirect:/team";
    }

    @GetMapping("/team/xinvaoteam")
    public String xinVaoTeam(@RequestParam Integer team, RedirectAttributes redirectAttributes,HttpServletRequest request){
        Teams t = teamdao.findById(team).get();
        User user = userUtils.getUserWithAuthority();
        if(t.getQuantity() == t.getActiveMember()){

        }
        if(detailDAO.countByUserAndTeams(user.getId(), team) > 0){
            redirectAttributes.addFlashAttribute("warning","Đang chờ duyệt");
            String referer = request.getHeader("Referer");
            return "redirect:" + referer;
        }
        Teamdetails teamdetails = new Teamdetails();
        teamdetails.setTeams(t);
        teamdetails.setUser(user);
        teamdetails.setStatus(false);
        teamdetails.setJoinDate(LocalDate.now());
        detailDAO.save(teamdetails);
        redirectAttributes.addFlashAttribute("success","Chờ trưởng nhóm duyệt");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/team/xoanhom")
    public String xoaNhom(@RequestParam Integer team, RedirectAttributes redirectAttributes, HttpServletRequest request){
        teamdao.deleteById(team);
        redirectAttributes.addFlashAttribute("success","Xóa nhóm thành công");
        return "redirect:/team" ;
    }

    @GetMapping("/team/quanly")
    public String quanLyTeam(@RequestParam Integer team, Model model){
        Teams teams = teamdao.findById(team).get();
        List<Teamdetails> thanhVien = new ArrayList<>();
        List<Teamdetails> choDuyet = new ArrayList<>();
        String infouser = null;
        User user = userUtils.getUserWithAuthority();
        Teamdetails teamdetails = null;
        for(Teamdetails p : teams.getTeamdetails()){
            if(p.getStatus() == false){
                choDuyet.add(p);
            }
            else{
                thanhVien.add(p);
                if(p.getUser().getId() == user.getId()){
                    infouser = p.getInfoUser();
                    teamdetails = p;
                }
            }
        }
        model.addAttribute("team", teams);
        model.addAttribute("thanhVien", thanhVien);
        model.addAttribute("choDuyet", choDuyet);
        model.addAttribute("infouser", infouser);
        model.addAttribute("detailId", teamdetails.getId());

        return "user/quanly-team";
    }

    @GetMapping("/team/detail")
    public String detailTeam(@RequestParam Integer team, Model model){
        Teams teams = teamdao.findById(team).get();
        List<Teamdetails> thanhVien = new ArrayList<>();
        String infouser = null;
        User user = userUtils.getUserWithAuthority();
        Teamdetails teamdetails = null;
        for(Teamdetails p : teams.getTeamdetails()){
            if(p.getStatus() == false){
            }
            else{
                thanhVien.add(p);
                if(p.getUser().getId() == user.getId()){
                    infouser = p.getInfoUser();
                    teamdetails = p;
                }
            }
        }
        model.addAttribute("team", teams);
        model.addAttribute("thanhVien", thanhVien);
        model.addAttribute("infouser", infouser);
        model.addAttribute("detailId", teamdetails.getId());

        return "user/team-detail";
    }

    @GetMapping("/team/duyetthanhvien")
    public String duyetThanhVien(@RequestParam Integer id, @RequestParam Integer team, Model model, RedirectAttributes redirectAttributes){
        Teams teams = teamdao.findById(team).get();
        if(teams.getQuantity() == teams.getActiveMember()){
            redirectAttributes.addFlashAttribute("error","Thành viên đã đủ, không thể duyệt thêm");
            return "redirect:/team/quanly";
        }
        Teamdetails td = detailDAO.findById(id).get();
        td.setStatus(true);
        td.setJoinDate(LocalDate.now());
        detailDAO.save(td);
        return "redirect:/team/quanly?team="+team;
    }

    @GetMapping("/team/xoathanhvien")
    public String xoaThanhVien(@RequestParam Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request){
        detailDAO.deleteById(id);
        redirectAttributes.addFlashAttribute("success","Xóa thành viên thành công");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/team/updateinfoUser")
    public String updateDescription(@RequestParam Integer detailId, @RequestParam String description, RedirectAttributes redirectAttributes, HttpServletRequest request){
        Teamdetails td = detailDAO.findById(detailId).get();
        td.setInfoUser(description);
        detailDAO.save(td);
        redirectAttributes.addFlashAttribute("success","Cập nhật thành công");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


    @GetMapping("/team/phongdoitruong")
    public String phongDoiTruong(@RequestParam Integer team, @RequestParam Long userId, RedirectAttributes redirectAttributes, HttpServletRequest request){
        Teams td = teamdao.findById(team).get();
        User user = userRepository.findById(userId).get();
        td.setUser(user);
        teamdao.save(td);
        return "redirect:/team";
    }

    @GetMapping("/team/teamdetail/{teamId}/exit")
    public String teamDetailExit(@PathVariable Integer teamId, RedirectAttributes redirectAttributes, HttpServletRequest request){
        Teamdetails td = detailDAO.findByUserAndTeam(userUtils.getUserWithAuthority().getId(),  teamId);
        detailDAO.delete(td);
        redirectAttributes.addFlashAttribute("success","Rời nhóm thành công");
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
