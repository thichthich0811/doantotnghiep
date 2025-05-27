package com.web.repository;

import com.web.entity.Eventweb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.sql.Date;
import java.util.List;
public interface EventDAO extends JpaRepository<Eventweb, Integer> {

    @Query(value = "SELECT COUNT(*) FROM eventweb;", nativeQuery = true)
    List<Object> CountEvent();

    @Query("SELECT e FROM Eventweb e WHERE e.dateStart >= ?1 and e.dateEnd <= ?2")
    List<Eventweb> findByDate(Date s, Date e);

    @Query(value = "( SELECT * FROM eventweb WHERE MONTH(date_start) = MONTH(CURRENT_DATE) AND YEAR(date_start) = YEAR(CURRENT_DATE) ORDER BY id DESC LIMIT 1 ) UNION ( SELECT * FROM eventweb ORDER BY id DESC LIMIT 1 ) LIMIT 1;", nativeQuery = true)
    public Eventweb lastEvent();

    @Query(value = "SELECT i.* from eventweb i where i.id != (select max(it.id) from eventweb it) and month(i.date_start) = month(current_date) and  year(i.date_start) = year(current_date) order by i.id desc limit 5", nativeQuery = true)
    public List<Eventweb> getEventIndex();

    @Query("select e from Eventweb e where e.nameEvent like ?1 order by e.id desc ")
    Page<Eventweb> findByParam(String search, Pageable pageable);
}
