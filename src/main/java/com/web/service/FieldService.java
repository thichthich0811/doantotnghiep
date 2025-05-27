package com.web.service;
import com.web.entity.Field;
import com.web.repository.FieldDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FieldService {
	@Autowired
	private FieldDAO fieldDAO;

	public List<Field> findFieldById(Integer id) {
		return fieldDAO.findFieldById(id);
	}
	public String findNameSporttypeById(Integer id) {
		return fieldDAO.findNameSporttypeById(id);
	}

}
