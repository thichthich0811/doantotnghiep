package com.web.service;
import com.web.repository.EventDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class EventService {
	@Autowired
	EventDAO eventDAO;


}
