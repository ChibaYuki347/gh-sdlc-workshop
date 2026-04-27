package com.example.crm.service;

import com.example.crm.dto.RenewalNotificationResponse;
import com.example.crm.model.Customer;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer createTestCustomer(Long id, String firstName, String lastName,
                                         int policyStatus, LocalDate endDate, String agentName) {
        Customer c = new Customer(firstName, lastName, firstName.toLowerCase() + "@example.com");
        c.id = id;
        c.policyNumber = "POL-" + id;
        c.policyType = "LIFE";
        c.policyStatus = policyStatus;
        c.premiumAmount = 10000;
        c.policyStartDate = Date.from(endDate.minusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        c.policyEndDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        c.agentName = agentName;
        c.agentEmail = agentName + "@example.co.jp";
        return c;
    }

    @Test
    void getRenewalNotifications_defaultDays_returnsMatchingCustomers() {
        LocalDate futureDate = LocalDate.now().plusDays(15);
        Customer c1 = createTestCustomer(1L, "太郎", "田中", 1, futureDate, "佐藤花子");

        when(customerRepository.findRenewalTargets(eq(1), any(Date.class)))
                .thenReturn(List.of(c1));

        RenewalNotificationResponse response = customerService.getRenewalNotifications(30, null);

        assertNotNull(response);
        assertEquals(1, response.totalCount);
        assertEquals(1, response.renewals.size());
        assertEquals("太郎", response.renewals.get(0).firstName);
    }

    @Test
    void getRenewalNotifications_withAgentName_callsFilteredQuery() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        Customer c1 = createTestCustomer(1L, "花子", "鈴木", 1, futureDate, "佐藤花子");

        when(customerRepository.findRenewalTargetsByAgent(eq(1), any(Date.class), eq("%佐藤花子%")))
                .thenReturn(List.of(c1));

        RenewalNotificationResponse response = customerService.getRenewalNotifications(30, "佐藤花子");

        assertEquals(1, response.totalCount);
        assertEquals("佐藤花子", response.renewals.get(0).agentName);
        verify(customerRepository).findRenewalTargetsByAgent(eq(1), any(Date.class), eq("%佐藤花子%"));
        verify(customerRepository, never()).findRenewalTargets(anyInt(), any(Date.class));
    }

    @Test
    void getRenewalNotifications_noResults_returnsEmptyList() {
        when(customerRepository.findRenewalTargets(eq(1), any(Date.class)))
                .thenReturn(Collections.emptyList());

        RenewalNotificationResponse response = customerService.getRenewalNotifications(30, null);

        assertNotNull(response);
        assertEquals(0, response.totalCount);
        assertTrue(response.renewals.isEmpty());
    }

    @Test
    void getRenewalNotifications_daysUntilRenewalCalculation_futureDate() {
        LocalDate futureDate = LocalDate.now().plusDays(20);
        Customer c1 = createTestCustomer(1L, "太郎", "田中", 1, futureDate, "佐藤花子");

        when(customerRepository.findRenewalTargets(eq(1), any(Date.class)))
                .thenReturn(List.of(c1));

        RenewalNotificationResponse response = customerService.getRenewalNotifications(30, null);

        assertEquals(20, response.renewals.get(0).daysUntilRenewal);
    }

    @Test
    void getRenewalNotifications_daysUntilRenewalCalculation_pastDate() {
        LocalDate pastDate = LocalDate.now().minusDays(5);
        Customer c1 = createTestCustomer(1L, "太郎", "田中", 1, pastDate, "佐藤花子");

        when(customerRepository.findRenewalTargets(eq(1), any(Date.class)))
                .thenReturn(List.of(c1));

        RenewalNotificationResponse response = customerService.getRenewalNotifications(30, null);

        assertEquals(-5, response.renewals.get(0).daysUntilRenewal);
    }

    @Test
    void getRenewalNotifications_daysUntilRenewalCalculation_today() {
        LocalDate today = LocalDate.now();
        Customer c1 = createTestCustomer(1L, "太郎", "田中", 1, today, "佐藤花子");

        when(customerRepository.findRenewalTargets(eq(1), any(Date.class)))
                .thenReturn(List.of(c1));

        RenewalNotificationResponse response = customerService.getRenewalNotifications(30, null);

        assertEquals(0, response.renewals.get(0).daysUntilRenewal);
    }

    @Test
    void getRenewalNotifications_dtoFieldMapping() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        Customer c1 = createTestCustomer(1L, "太郎", "田中", 1, futureDate, "佐藤花子");
        c1.phone = "03-1234-5678";

        when(customerRepository.findRenewalTargets(eq(1), any(Date.class)))
                .thenReturn(List.of(c1));

        RenewalNotificationResponse response = customerService.getRenewalNotifications(30, null);

        var dto = response.renewals.get(0);
        assertEquals(1L, dto.customerId);
        assertEquals("太郎", dto.firstName);
        assertEquals("田中", dto.lastName);
        assertEquals("太郎 田中", dto.fullName);
        assertEquals("太郎@example.com", dto.email);
        assertEquals("03-1234-5678", dto.phone);
        assertEquals("POL-1", dto.policyNumber);
        assertEquals("LIFE", dto.policyType);
        assertEquals(1, dto.policyStatus);
        assertEquals("有効", dto.policyStatusText);
        assertEquals(10000, dto.premiumAmount);
        assertEquals("佐藤花子", dto.agentName);
    }
}
