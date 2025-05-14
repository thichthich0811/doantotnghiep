package com.web.service;


import com.web.entity.Shifts;
import com.web.repository.ShiftDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ShiftService {

	@Autowired
	ShiftDAO shiftDAO;

}
