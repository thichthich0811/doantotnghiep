package com.web.service;

import java.util.List;

import com.web.entity.Bookingdetails;
import com.web.repository.BookingDetailDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@SuppressWarnings("unused")
@Service
public class BookingDetailService {
	@Autowired
	BookingDetailDAO bookingDetailDAO;


}
