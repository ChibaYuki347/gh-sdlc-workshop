package com.zurich.crm.repository;

import com.zurich.crm.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    List<Policy> findByCustomerId(Long customerId);

    List<Policy> findByStatus(int status);

    Policy findByPolicyNumber(String policyNumber);
}
