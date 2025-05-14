package com.web.service;


import com.web.entity.Teams;
import com.web.repository.TeamDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class TeamService {
	@Autowired
	TeamDAO teamDAO;
}
