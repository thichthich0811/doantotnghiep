package com.web.controller.user;

import com.web.entity.*;
import com.web.repository.*;
import com.web.service.FieldService;
import com.web.service.ShiftService;
import com.web.service.VoucherService;
import com.web.utils.BookingStatus;
import com.web.utils.UserUtils;
import com.web.vnpay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class FieldController {
    String userlogin = null;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private FieldDAO fieldDAO;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private BookingDAO bookingDAO;
    @Autowired
    private BookingDetailDAO bookingDetailDAO;
    @Autowired
    private SportTypeDAO sportTypeDAO;
    @Autowired
    private ShiftDAO shiftDAO;
    @Autowired
    private VoucherDAO voucherDAO;
    private Date dateselect;
    private LocalTime time = null;

    @GetMapping("/field-detail")
    public String viewDetail(Model model, @RequestParam("id") Integer id, HttpServletRequest request) {
        userlogin = (String) request.getSession().getAttribute("username");
        Field field = fieldDAO.findById(id).get();
        List<Field> list = fieldDAO.findByType(field.getSporttype().getId());

        model.addAttribute("field", field);
        model.addAttribute("listField", list);
        model.addAttribute("nameSportype", field.getSporttype().getCategoryName());
        return "user/san-single";
    }

    @PostMapping("/field/detail/check")
    public String searchShiftDefault(Model model, @RequestParam("id") Integer idField,
            @RequestParam("dateInput") Date date) {
        dateselect = date; // Ngày được chọn
        List<Shifts> shiftsempty = shiftDAO.findShiftDate(idField, date); // List ca trống thỏa mản điều kiện đầu vào
        LocalTime currentTime = LocalTime.now(); // Lấy giờ hiện tại
        LocalDate currentDate = LocalDate.now(); // lấy ngày hiện tại
        LocalDate selectedDate = date.toLocalDate(); // Parse date về kiểu LocalDate
        if (selectedDate.equals(currentDate)) { // Nếu ngày chọn = ngày hiện tại
            List<Shifts> shiftsNull = new ArrayList<>(); // Ca trống
            for (Shifts shift : shiftsempty) {
                LocalTime shiftStartTime = shift.getStartTime(); // Lấy thời gian bắt đầu của ca làm việc
                if (shiftStartTime.isAfter(currentTime)) { // Kiểm tra thời gian bắt đầu của ca làm việc
                    shiftsNull.add(shift); // Thêm vào danh sách nếu thỏa mãn điều kiện
                }
            }
            model.addAttribute("shiftsNull", shiftsNull);
        } else { // Đổ tất cả các ca trống
            model.addAttribute("shiftsNull", shiftsempty);
        }
        // Format yyyy-mm-dd thành dd-mm-yyyy
        LocalDate dateformat = date.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = dateformat.format(formatter);
        // Dữ liệu hiển thị trong trang Detail

        model.addAttribute("date", date);
        model.addAttribute("formattedDate", formattedDate);

        Field field = fieldDAO.findById(idField).get();
        List<Field> list = fieldDAO.findByType(field.getSporttype().getId());
        model.addAttribute("field", field);
        model.addAttribute("listField", list);
        model.addAttribute("nameSportype", field.getSporttype().getCategoryName());
        return "user/san-single";
    }

    @GetMapping("/field/booking/{idField}")
    public String Booking(RedirectAttributes redirectAttributes, Model model,
            @RequestParam(value = "nameshift", required = false) String nameShift,
            @PathVariable Integer idField,
            @RequestParam(value = "voucher", required = false) String voucher,
            @RequestParam(name = "note", required = false) String note) {
        int discountpercent = 0; // Biến phần trăm giảm giá
        double pricevoucher = 0; // Biến tiền được giảm
        double totalprice = 0; // Biến tạm tính
        double thanhtien = 0; // Biến thành tiền
        double tiencoc = 0; // Biến tiền cọc
        double conlai = 0; // Biến tiền còn lại
        int shiftid = 0; // Biến id ca
        List<Shifts> shiftsempty = shiftDAO.findShiftDate(idField, dateselect); // List ca trống thỏa mản điều kiện đầu
                                                                                // vào
        List<Shifts> shift = shiftDAO.findShiftByName(nameShift); // Ca được chọn ở detail
        boolean hasOverlap = shiftsempty.stream()
                .anyMatch(s1 -> shift.stream()
                        .anyMatch(s2 -> s1.getId().equals(s2.getId())));
        if (!hasOverlap) {
            redirectAttributes.addFlashAttribute("error", "Sân đã được đặt");
            return "redirect:/field";
        }
        for (int i = 0; i < shift.size(); i++) {
            time = shift.get(i).getStartTime(); // giờ bắt đầu của ca được chọn
            shiftid = shift.get(i).getId(); // id ca được chọn
        }
        // Khởi tạo giờ 17:00
        LocalTime timeToCompare = LocalTime.of(17, 0); // Khởi tạo 17h00
        LocalTime timeToSix = LocalTime.of(06, 0); // Khởi tạo 6h00
        LocalDate currentDate = LocalDate.now(); // Ngày hiện tại
        // Chuyển đổi chuỗi ngày thành đối tượng LocalDate
        LocalDate localDate = dateselect.toLocalDate(); // chuyển String => LocalDate
        // Định dạng thành chuỗi "dd/MM/yyyy"
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        double phuthu = 0; // giá phụ thu
        userlogin = userUtils.getUserWithAuthority().getEmail();
        if (userlogin == null) {
            // Người dùng không tồn tại hoặc thông tin đăng nhập không chính xác
            return "redirect:/login";
        } else {
            // Kiểm tra đã có tài khoản đăng nhập
            List<Field> fieldListById = fieldService.findFieldById(idField); // Tìm sân theo id đã chọn
            double giasan = fieldListById.get(0).getPrice(); // giá tiền gốc của sân
            String nameSportype = fieldService.findNameSporttypeById(idField); // Tìm tên môn thể thao của sân đã chọn
            List<Voucher> magiamgia = voucherDAO.findAll(); // List mã giảm giá
            if (time.isAfter(timeToCompare) || time.isBefore(timeToSix)) { // Kiểm tra điều kiện phụ thu nếu có
                phuthu = giasan * 30 / 100; // Tiền phụ thu
                totalprice = giasan + phuthu; // Tiền tạm tính
                model.addAttribute("totalprice", totalprice);
                model.addAttribute("phuthu", phuthu);
            } else { // Không có phụ thu
                totalprice = giasan;
                model.addAttribute("totalprice", totalprice);
                model.addAttribute("phuthu", phuthu);
            }
            if (voucher == null) { // Nếu không có nhập
                thanhtien = totalprice;
                tiencoc = thanhtien * 30 / 100;
                conlai = thanhtien - tiencoc;

            } else { // Nếu có voucher
                model.addAttribute("voucher", voucher);
                for (int i = 0; i < magiamgia.size(); i++) {
                    // Trong đoạn mã của bạn
                    Date startDateSql = (Date) magiamgia.get(i).getStartDate(); // Ngày bắt đầu mã giảm giá
                    LocalDate startDate = startDateSql.toLocalDate(); // Chuyển về LocalDate
                    Date endDateSql = (Date) magiamgia.get(i).getEndDate(); // Ngày kết thúc mã giảm giá
                    LocalDate endDate = endDateSql.toLocalDate(); // Chuyển về LocalDate
                    // Nếu mã giảm giá thỏa mãn điều kiện
                    if (voucher.equals(magiamgia.get(i).getCode()) && startDate.isBefore(currentDate)
                            && endDate.isAfter(currentDate)) {
                        discountpercent = magiamgia.get(i).getDiscountPercent(); // phần trăm được giảm
                        pricevoucher = totalprice * discountpercent / 100; // tiền được giảm
                        thanhtien = totalprice - pricevoucher; // thành tiền
                        tiencoc = thanhtien * 30 / 100; // tiền phải cọc đặt sân
                        conlai = thanhtien - tiencoc; // tiền còn lại
                        model.addAttribute("thongbaovoucher", "Mã đã được áp dụng giảm " + discountpercent + "%");
                        break;
                    } else if (!voucher.equals(magiamgia.get(i).getId())) { // nếu mã giảm giá không hợp lệ
                        thanhtien = totalprice - pricevoucher;
                        tiencoc = thanhtien * 30 / 100;
                        conlai = thanhtien - tiencoc;
                        model.addAttribute("thongbaovoucher", "Mã không hợp lệ");
                    } // Mã giảm giá không đúng thời hạn
                    else if (startDate.isAfter(currentDate)
                            || endDate.isBefore(currentDate) && voucher.equals(magiamgia.get(i).getId())) {
                        thanhtien = totalprice - pricevoucher;
                        tiencoc = thanhtien * 30 / 100;
                        conlai = thanhtien - tiencoc;
                        model.addAttribute("thongbaovoucher", "Mã không áp dụng hôm nay");
                        break;
                    }
                }
            }
            // Dữ liệu đổ lại giao diện
            model.addAttribute("note", note);
            model.addAttribute("shiftid", shiftid);
            model.addAttribute("pricevoucher", pricevoucher);
            model.addAttribute("conlai", conlai);
            model.addAttribute("tiencoc", tiencoc);
            model.addAttribute("thanhtien", thanhtien);
            model.addAttribute("listvoucher", magiamgia);
            model.addAttribute("nameShift", nameShift);
            model.addAttribute("dateselect", dateselect);
            model.addAttribute("nameSportype", nameSportype);
            model.addAttribute("formattedDate", formattedDate);
            model.addAttribute("fieldListById", fieldListById);

        }
        // Gọi trang checkout
        return "user/checkout-dat-san";
    }

    @PostMapping("/getIp/create")
    public String createPayment(
            @RequestParam("amount") String inputMoney, HttpServletRequest request, HttpSession session,
            @RequestParam("thanhtien") Double bookingprice, @RequestParam("phone") String phone,
            @RequestParam("note") String note, @RequestParam("shiftid") int shiftid,
            @RequestParam("fieldid") int fieldid, @RequestParam("playdate") Date playdateSt,
            @RequestParam("pricefield") Double priceField) {
        Bookings newbooking = new Bookings();
        Bookingdetails newbookingdetail = new Bookingdetails();
        User user = userUtils.getUserWithAuthority();
        newbooking.setUser(user);
        newbooking.setBookingDate(new Date(System.currentTimeMillis()));
        newbooking.setBookingPrice(bookingprice);
        newbooking.setPhone(phone);
        newbooking.setFullName(user.getFullName());
        newbooking.setNote(note);
        newbooking.setBookingStatus(BookingStatus.DA_COC);

        newbookingdetail.setPlaydate(playdateSt);
        newbookingdetail.setPrice(bookingprice);
        newbookingdetail.setField(fieldDAO.findById(fieldid).get());
        newbookingdetail.setShifts(shiftDAO.findById(shiftid).get());
        session.setAttribute("booking", newbooking);
        session.setAttribute("bookingDetail", newbookingdetail);
        String orderId = String.valueOf(System.currentTimeMillis());
        String vnpayUrl = vnPayService.createOrder(bookingprice.intValue() * 30 / 100, orderId,
                "http://localhost:8080/checkpayment");

        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/checkpayment")
    public String checkPayment(HttpServletRequest request, RedirectAttributes redirectAttributes, HttpSession session) {
        int check = vnPayService.orderReturn(request);
        if (check == 1) {
            Bookings bookings = (Bookings) session.getAttribute("booking");
            Bookingdetails bookingDetail = (Bookingdetails) session.getAttribute("bookingDetail");
            bookingDAO.save(bookings);
            bookingDetail.setBookings(bookings);
            bookingDetailDAO.save(bookingDetail);
            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công");
            return "redirect:/taikhoan#history";
        } else {
            return "redirect:/payment-error";
        }
    }

    @GetMapping("/payment-error")
    public String errorPayemtn(RedirectAttributes redirectAttributes, HttpSession session) {
        session.removeAttribute("booking");
        session.removeAttribute("bookingDetail");
        redirectAttributes.addFlashAttribute("error", "Thanh toán thất bại");
        return "user/errorpayment";
    }

    @GetMapping("/field")
    public String viewField(Model model, @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) Date date, @RequestParam(required = false) Integer shiftId) {
        List<Field> list = null;
        if (typeId == null && date == null && shiftId == null) {
            list = fieldDAO.findAllActive();
        }
        if (typeId != null && date == null && shiftId == null) {
            list = fieldDAO.findBySporttypeIdActive(typeId);
        }
        if (typeId != null && date != null && shiftId != null) {
            list = fieldDAO.findSearch(date, typeId, shiftId);
            model.addAttribute("thongbao", "Kết quả tìm kiếm sân trống: ");
            model.addAttribute("namesporttype", sportTypeDAO.findById(typeId).get().getCategoryName());
            model.addAttribute("nameshift", shiftDAO.findById(shiftId).get().getNameShift());
            model.addAttribute("formattedDate", date);

        }
        model.addAttribute("listField", list);
        model.addAttribute("sporttype", sportTypeDAO.findAll());
        model.addAttribute("shift", shiftDAO.findAll());
        return "user/san";
    }
}
