package com.example.crm.repository;

import com.example.crm.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    @Query("SELECT c FROM Customer c WHERE c.policyStatus = 1")
    List<Customer> findActiveCustomers();

    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    Customer findByEmailAddress(String email);
}
