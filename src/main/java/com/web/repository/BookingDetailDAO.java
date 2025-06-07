package com.web.repository;

import com.web.entity.Bookingdetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingDetailDAO extends JpaRepository<Bookingdetails, Integer> {
	// top 3 san được dat nhiều nhất
	@Query(value = "select f.name_field, f.price,\n" +
			"(SELECT count(bk.id) from bookingdetails bk where bk.field_id = f.id) as solandat,\n" +
			"(SELECT sum(bk.price) from bookingdetails bk where bk.field_id = f.id) as doanhthu\n" +
			"from field f order by solandat desc limit 3", nativeQuery = true)
	List<Object[]> top3SanDatNhieu();

}
