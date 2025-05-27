package com.web.repository;

import com.web.entity.Sporttype;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportTypeDAO extends JpaRepository<Sporttype, Integer> {
}
