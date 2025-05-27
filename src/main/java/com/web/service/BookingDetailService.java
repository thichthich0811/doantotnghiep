package com.web.service;

import com.web.repository.BookingDetailDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingDetailService {
	@Autowired
	BookingDetailDAO bookingDetailDAO;
}
