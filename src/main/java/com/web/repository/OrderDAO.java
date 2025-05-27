package com.web.repository;
import com.web.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.sql.Date;
import java.util.List;

public interface OrderDAO extends JpaRepository<Orders, Integer> {

	@Query("select count(o.id) from Orders o where o.createDate = current_date ")
	int countOrderInDate();

	@Query("select o from Orders o where o.user.id = ?1")
    List<Orders> findByUser(Long id);

	@Query("select i from Orders i where i.createDate between ?1 and ?2")
    List<Orders> findByDate(Date from, Date to);

	@Query("select i from Orders i where i.createDate between ?1 and ?2 and i.paymentStatus = ?3")
	List<Orders> findByDateAndThanhToan(Date from, Date to, Boolean thanhToan);

	@Query("select i from Orders i where i.createDate between ?1 and ?2 and i.orderStatus = ?3")
	List<Orders> findByDateAndTrangThaiDonHang(Date from, Date to, String trangThaiDonHang);

	@Query("select i from Orders i where i.createDate between ?1 and ?2 and i.orderStatus = ?3 and i.paymentStatus = ?4")
	List<Orders> findByAll(Date from, Date to, String trangThaiDonHang, Boolean trangThaiThanhToan);

	@Query(value = "select sum(o.total_price) from orders o WHERE month(o.create_date) = ?1 and year(o.create_date) = ?2 and o.payment_status = 1", nativeQuery = true)
	Double calDt(int i, Integer year);
}
