package com.example.crm.repository;

import com.example.crm.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    @Query("SELECT c FROM Customer c WHERE c.policyStatus = 1")
    List<Customer> findActiveCustomers();

    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    Customer findByEmailAddress(String email);

    @Query("SELECT c FROM Customer c WHERE c.policyStatus = 1 " +
           "AND c.policyEndDate IS NOT NULL " +
           "AND c.policyEndDate BETWEEN :today AND :expiryDate " +
           "ORDER BY c.policyEndDate ASC")
    List<Customer> findUpcomingRenewals(
        @Param("today") Date today,
        @Param("expiryDate") Date expiryDate);

    @Query("SELECT c FROM Customer c WHERE c.policyStatus = 1 " +
           "AND c.policyEndDate IS NOT NULL " +
           "AND c.policyEndDate BETWEEN :today AND :expiryDate " +
           "AND c.agentName = :agentName " +
           "ORDER BY c.policyEndDate ASC")
    List<Customer> findUpcomingRenewalsByAgent(
        @Param("today") Date today,
        @Param("expiryDate") Date expiryDate,
        @Param("agentName") String agentName);
}
