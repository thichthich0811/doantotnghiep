package com.web.service;
import com.web.repository.OrderDetailDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailService {
	@Autowired
	OrderDetailDAO orderDetailDAO;
}
