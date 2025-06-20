package org.unibl.etf.sigurnost.insurancesystem.service;

import org.unibl.etf.sigurnost.insurancesystem.model.Policy;

import java.util.List;

public interface PolicyService {
    List<Policy> findAll();
    Policy create(Policy policy);
    void delete(Long id);
    public Policy update(Long id, Policy updatedPolicy);
}
