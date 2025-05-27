package com.web.service;

import com.web.repository.SportTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SportTypeService {
	@Autowired
	SportTypeDAO sportTypeDAO;

}
