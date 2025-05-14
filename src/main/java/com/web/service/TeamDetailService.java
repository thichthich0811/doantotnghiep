package com.web.service;


import com.web.entity.Teamdetails;
import com.web.repository.TeamDetailDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class TeamDetailService {
	@Autowired
	TeamDetailDAO teamDetailDAO;

}
