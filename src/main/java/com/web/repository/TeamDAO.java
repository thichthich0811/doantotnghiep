package com.web.repository;


import com.web.entity.Teams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface TeamDAO extends JpaRepository<Teams, Integer> {

	@Query("select t from Teams t where t.nameTeam like ?1")
	Page<Teams> findByParam(String searchText, Pageable pageable);

	@Query("select t from Teams t where t.sportType.id = ?1")
	Page<Teams> findBySporttype(Integer sporttypeid, Pageable pageable);
}
