package com.web.service;

import com.web.repository.TeamDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TeamService {
	@Autowired
	TeamDAO teamDAO;
}
