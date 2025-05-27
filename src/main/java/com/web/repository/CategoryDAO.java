package com.web.repository;

import com.web.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Categories, Integer> {

}
