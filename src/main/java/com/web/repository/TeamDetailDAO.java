package com.web.repository;


import com.web.entity.Teamdetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@SuppressWarnings("unused")
public interface TeamDetailDAO extends JpaRepository<Teamdetails, Integer> {

	@Query("select count(td.id) from Teamdetails td where td.user.id = ?1 and td.teams.id = ?2")
	Long countByUserAndTeams(Long userId, Integer teamId);

	@Query("select t from Teamdetails t where t.user.id = ?1 and t.teams.id = ?2")
    Teamdetails findByUserAndTeam(Long id, Integer teamId);
}
