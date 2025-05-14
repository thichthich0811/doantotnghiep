package com.web.service;


import com.web.entity.Orderdetails;
import com.web.repository.OrderDetailDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class OrderDetailService {
	@Autowired
	OrderDetailDAO orderDetailDAO;
}
