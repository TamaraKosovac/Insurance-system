package org.unibl.etf.sigurnost.insurancesystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unibl.etf.sigurnost.insurancesystem.model.Policy;
import org.unibl.etf.sigurnost.insurancesystem.repository.PolicyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;

    @Override
    public List<Policy> findAll() {
        return policyRepository.findAll();
    }

    @Override
    public Policy create(Policy policy) {
        return policyRepository.save(policy);
    }

    @Override
    public void delete(Long id) {
        policyRepository.deleteById(id);
    }

    @Override
    public Policy update(Long id, Policy updatedPolicy) {
        return policyRepository.findById(id)
                .map(existingPolicy -> {
                    existingPolicy.setName(updatedPolicy.getName());
                    existingPolicy.setType(updatedPolicy.getType());
                    existingPolicy.setAmount(updatedPolicy.getAmount());
                    existingPolicy.setDescription(updatedPolicy.getDescription());
                    return policyRepository.save(existingPolicy);
                })
                .orElseThrow(() -> new RuntimeException("Policy not found with ID: " + id));
    }
}
