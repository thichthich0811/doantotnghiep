package com.web.service;


import com.web.entity.Products;
import com.web.repository.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class ProductService  {
	@Autowired
	ProductDAO productDAO;

}
