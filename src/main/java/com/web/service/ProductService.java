package com.web.service;

import com.web.repository.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService  {
	@Autowired
	private ProductDAO productDAO;

}
