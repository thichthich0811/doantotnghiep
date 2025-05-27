package com.web.repository;

import com.web.entity.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.sql.Date;
import java.util.List;

public interface ContactDAO extends JpaRepository<Contacts, Integer> {

	@Query("select c from Contacts c where c.datecontact between ?1 and ?2 order by c.id desc ")
	List<Contacts> findAllByDate(Date from, Date to);

	@Query("select c from Contacts c where c.datecontact = current_date order by c.id desc ")
	List<Contacts> findAllByDate();
}
