package com.web.repository;

import com.web.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select g from Cart g where g.user.id = ?1")
    List<Cart> findByUser(Long userId);

    @Query("select g from Cart g where g.user.id = ?1 and g.products.id = ?2")
    Optional<Cart> findByUserAndIdSp(Long userId, Integer idSp);

    @Query("select count(g.id) from Cart g where g.user.id = ?1")
    Long soLuongGh(Long userId);
}

