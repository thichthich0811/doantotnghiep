package com.web.repository;
import java.sql.Date;
import java.util.List;
import com.web.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FieldDAO extends JpaRepository<Field, Integer>{
	
	@Query(value="SELECT COUNT(*) FROM field;", nativeQuery = true)
	List<Object> CountField();

	@Query(value = "select f from Field f where f.sporttype.id = ?1")
	List<Field> findBySporttypeId(Integer cid);

	@Query(value = "select f from Field f where f.sporttype.id = ?1 and f.status = true")
	List<Field> findBySporttypeIdActive(Integer cid);
	
	@Query(value="SELECT f.*\r\n"
			+ "FROM field f \r\n"
			+ "LEFT JOIN bookingdetails bd ON f.id = bd.field_id AND bd.playdate = ?1 and bd.shifts_id = ?3 and f.sporttype_id = ?2 \r\n"
			+ "WHERE bd.field_id IS NULL and f.sporttype_id = ?2 And status = 1", nativeQuery = true)
	List<Field> findSearch(Date dateInput, Integer categorySelect, Integer shiftSelect);
	
	@Query(value="select * from field where id = :id", nativeQuery = true)
	List<Field> findFieldById(Integer id);
	
	@Query(value="SELECT f.sporttype.categoryName FROM Field f WHERE f.id = ?1")
	String findNameSporttypeById(Integer id);
	
	// admin
	@Query(value = "select * from field where status = 1", nativeQuery = true)
	List<Field> findAllActive();

	@Query("select count(f.id) from Field f where f.status = true")
	int countFieldActiving();

	@Query("select f from Field f where f.sporttype.id = ?1 and f.status <> false")
	List<Field> findByType(Integer id);

	@Query("select f from Field f where f.status <> false")
	List<Field> findAll();
}
