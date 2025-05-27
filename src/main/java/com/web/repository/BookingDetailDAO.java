package com.web.repository;

import com.web.entity.Bookingdetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingDetailDAO extends JpaRepository<Bookingdetails, Integer> {

	@Query(value = "SELECT  s.* FROM bookingdetails bd\r\n"
			+ "JOIN field s ON bd.field_id = s.id\r\n"
			+ "GROUP BY bd.field_id\r\n"
			+ "ORDER BY COUNT(*) DESC\r\n"
			, nativeQuery = true)
	List<Object[]> findTopFieldsWithMostBookings();

    @Query(value = "SELECT * FROM bookingdetails WHERE bookingid = :bookingid", nativeQuery = true)
    List<Bookingdetails> detailBooking(@Param("bookingid") Integer bookingid);
    // dashboard 
   
    // top 3 san được dat nhiều nhất
    @Query(value = "select f.name_field, f.price,\n" +
			"(SELECT count(bk.id) from bookingdetails bk where bk.field_id = f.id) as solandat,\n" +
			"(SELECT sum(bk.price) from bookingdetails bk where bk.field_id = f.id) as doanhthu\n" +
			"from field f order by solandat desc limit 3", nativeQuery = true)
    List<Object[]> top3SanDatNhieu();

    // top 5 dăt san 
    @Query(value = "SELECT\r\n"
    		+ "    u.firstname,\r\n"
    		+ "    u.lastname,\r\n"
    		+ "    u.phone,\r\n"
    		+ "    COUNT(b.bookingid) AS booking_count,\r\n"
    		+ "    SUM(CASE\r\n"
    		+ "        WHEN b.bookingstatus IN ('Hoàn Thành', 'Đã Cọc') THEN b.bookingprice * 0.3\r\n"
    		+ "        ELSE 0\r\n"
    		+ "    END) AS total_revenue\r\n"
    		+ "FROM\r\n"
    		+ "    users u\r\n"
    		+ "JOIN\r\n"
    		+ "    bookings b ON u.username = b.username\r\n"
    		+ "GROUP BY\r\n"
    		+ "    u.username, u.firstname, u.lastname, u.phone\r\n"
    		+ "ORDER BY\r\n"
    		+ "    booking_count DESC\r\n"
    		+ "LIMIT 5;" , nativeQuery = true)
    List<Object[]> top5UserDatSan();
}
