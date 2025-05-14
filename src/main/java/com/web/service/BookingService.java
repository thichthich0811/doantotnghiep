package com.web.service;

import com.web.entity.Bookings;
import com.web.repository.BookingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class BookingService {

	@Autowired
	BookingDAO bookingDAO;

	
}
