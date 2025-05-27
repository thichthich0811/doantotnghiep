package com.web.service;

import com.web.repository.BookingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

	@Autowired
	BookingDAO bookingDAO;

	
}
