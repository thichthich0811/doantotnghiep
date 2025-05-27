package com.web.repository;

import java.sql.Date;
import java.util.List;

import com.web.entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingDAO extends JpaRepository<Bookings, Long> {

	// Đếm số lượng hóa đơn trong ngày
	@Query("select count(o.id) from Bookings o where o.bookingDate = current_date ")
	int countBookingInDate();

	@Query("select b from Bookings b where b.user.id = ?1")
    List<Bookings> findByUser(Long id);

	@Query("select b from Bookings b where b.bookingDate between ?1 and ?2")
    List<Bookings> findByDate(Date from, Date to);

	@Query("select b from Bookings b where b.bookingDate between ?1 and ?2 and b.bookingStatus = ?3")
	List<Bookings> findByDateAndTrangThai(Date from, Date to, String trangThaiThanhToan);

	@Query(value = "SELECT \n" +
			"    IFNULL((\n" +
			"        SELECT SUM(b.booking_price) FROM bookings b WHERE MONTH(b.booking_date) = ?1 \n" +
			"        AND YEAR(b.booking_date) = ?2 AND b.booking_status = 'Đã Thanh Toán'\n" +
			"    ), 0)\n" +
			"    +\n" +
			"    IFNULL((\n" +
			"        SELECT SUM(b.booking_price * 0.3) FROM bookings b WHERE MONTH(b.booking_date) = ?1 \n" +
			"          AND YEAR(b.booking_date) = ?2 AND b.booking_status = 'Đã Cọc'\n" +
			"    ), 0) AS tong\n",nativeQuery = true)
	Double calDt(int i, Integer year);
}
