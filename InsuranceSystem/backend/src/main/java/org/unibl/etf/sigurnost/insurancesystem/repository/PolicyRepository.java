package org.unibl.etf.sigurnost.insurancesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.sigurnost.insurancesystem.model.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
}
