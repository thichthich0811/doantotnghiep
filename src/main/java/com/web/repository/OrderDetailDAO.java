package com.web.repository;


import com.web.entity.Orderdetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailDAO extends JpaRepository<Orderdetails, Integer> {

}
