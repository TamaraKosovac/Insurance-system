package org.unibl.etf.sigurnost.insurancesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.sigurnost.insurancesystem.model.Purchase;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    boolean existsByTransactionId(String transactionId);
    Optional<Purchase> findByTransactionId(String transactionId);
    List<Purchase> findByUserId(Long userId);
}