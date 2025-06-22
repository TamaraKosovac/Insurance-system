package org.unibl.etf.sigurnost.insurancesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.sigurnost.insurancesystem.model.RevokedToken;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {
    boolean existsByToken(String token);
}
