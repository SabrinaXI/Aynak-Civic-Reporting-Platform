package com.capstoneb.aynak.userservice2.repository;

import com.capstoneb.aynak.userservice2.model.PointsLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointsLedgerRepo extends JpaRepository<PointsLedger, Long> {

	Optional<PointsLedger> findByCitizenIdAndReasonAndReferenceId(Integer citizenId, String reason, Integer referenceId);
}
