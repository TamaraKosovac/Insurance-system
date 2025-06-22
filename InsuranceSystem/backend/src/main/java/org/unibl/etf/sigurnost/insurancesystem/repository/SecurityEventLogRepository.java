package org.unibl.etf.sigurnost.insurancesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.sigurnost.insurancesystem.model.SecurityEventLog;

public interface SecurityEventLogRepository extends JpaRepository<SecurityEventLog, Long> {
}
