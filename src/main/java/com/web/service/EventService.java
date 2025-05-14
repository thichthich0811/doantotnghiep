package com.web.service;

//import java.awt.Event;

import com.web.entity.Eventweb;
import com.web.repository.EventDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("unused")
@Service
public class EventService {

	@Autowired
	EventDAO eventDAO;


}
