package com.web.repository;

import com.web.entity.Orderdetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailDAO extends JpaRepository<Orderdetails, Integer> {

}
