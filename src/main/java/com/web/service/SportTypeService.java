package com.web.service;


import com.web.entity.Sporttype;
import com.web.repository.SportTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class SportTypeService {
	@Autowired
	SportTypeDAO sportTypeDAO;

}
