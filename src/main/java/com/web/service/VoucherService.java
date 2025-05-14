package com.web.service;


import com.web.entity.Voucher;
import com.web.repository.VoucherDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class VoucherService {
	@Autowired
	VoucherDAO voucherDAO;

}
