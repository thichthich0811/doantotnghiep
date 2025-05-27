package com.web.repository;
import com.web.entity.Shifts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.sql.Date;
import java.util.List;

public interface ShiftDAO extends JpaRepository<Shifts, Integer> {

		@Query(value="select s from Shifts s where s.nameShift LIKE :name")
		List<Shifts> findShiftByName(String name);

		@Query(value="SELECT s FROM Shifts s WHERE s.id NOT IN ( SELECT b.shifts.id FROM Bookingdetails b WHERE b.field.id = ?1 AND b.playdate = ?2 )")
		List<Shifts> findShiftDate(Integer id, Date date);
}
