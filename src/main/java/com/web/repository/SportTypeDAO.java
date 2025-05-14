package com.web.repository;


import com.web.entity.Sporttype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface SportTypeDAO extends JpaRepository<Sporttype, Integer> {
}
