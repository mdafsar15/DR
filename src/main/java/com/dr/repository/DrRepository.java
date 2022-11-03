package com.dr.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dr.model.DrBlkTransLog;

@Repository
public interface DrRepository extends JpaRepository<DrBlkTransLog, Long> {

	@Query(value = "SELECT * FROM blc_transaction_log where status=:status Order By log_id ASC LIMIT 1", nativeQuery = true)
	public DrBlkTransLog findSingleRecord(String status);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "UPDATE blc_transaction_log SET status=:status,modified_at=:currentTime WHERE log_id=:id", nativeQuery = true)
	public Integer updateSingleRecord(String status, LocalDateTime currentTime, Long id);

	List<DrBlkTransLog> findByStatus(String status);

}
