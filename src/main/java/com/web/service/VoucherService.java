package com.web.service;

import com.web.repository.VoucherDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoucherService {
	@Autowired
	VoucherDAO voucherDAO;

}
