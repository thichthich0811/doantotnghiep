package com.web.dto;


import com.web.entity.Products;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification implements Specification<Products> {

    private List<Long> categoryIds;
    private Double minPrice;
    private Double maxPrice;

    public ProductSpecification(List<Long> categoryIds, Double minPrice, Double maxPrice) {
        this.categoryIds = categoryIds;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public Predicate toPredicate(Root<Products> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        if (categoryIds != null && !categoryIds.isEmpty()) {
            predicate = cb.and(predicate, root.get("categories").get("id").in(categoryIds));
        }
        if (minPrice != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        predicate = cb.and(predicate, cb.notEqual(root.get("productStatus"), false));
        return predicate;
    }
}
