package com.web.repository;
import com.web.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VoucherDAO extends JpaRepository<Voucher, Integer> {

	@Query("select v from Voucher v where v.endDate >= current_date  and v.startDate <= current_date ")
	List<Voucher> fillActive();

	@Query("select v from Voucher v where v.endDate < current_date ")
	List<Voucher> fillInActive();

	@Query("Select v From Voucher v Where v.endDate > current_date  and v.startDate > current_date ")
	List<Voucher> fillWillActive();

	@Query("select v from Voucher v where v.code = ?1")
	Optional<Voucher> findByCode(String code);

	@Query("select v from Voucher v where v.code = ?1 and v.id <> ?2")
	Optional<Voucher> findByCodeAndId(String code, Integer id);
}
