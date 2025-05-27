package com.web.repository;

import com.web.entity.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductDAO extends JpaRepository<Products, Integer>, JpaSpecificationExecutor<Products> {

	@Query("select p from Products p where p.categories.id = ?1")
    List<Products> findByCategory(Long category);

	@Query("select p from Products p where p.productStatus <> false")
	Page<Products> findAll(Pageable pageable);

	@Query("select count(p.id) from Products p where p.productStatus = true")
    Long countProductActive();

	@Query(value = "select p.product_name,p.price,\n" +
			"(SELECT sum(bk.quantity) from orderdetails bk where bk.products_id = p.id) as soluongban,\n" +
			"(SELECT sum(bk.quantity * bk.price) from orderdetails bk where bk.products_id = p.id) as doanhthu\n" +
			"from products p order by soluongban desc limit 3", nativeQuery = true)
	List<Object[]> top3BanChay();
}
