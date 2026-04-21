package com.example.crm.service;

import com.example.crm.model.Customer;
import com.example.crm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RenewalNotificationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RenewalNotificationService renewalNotificationService;

    private Customer buildCustomer(long id, String firstName, String lastName,
                                   String agentName, int daysUntilExpiry) {
        Customer c = new Customer();
        c.id = id;
        c.firstName = firstName;
        c.lastName = lastName;
        c.email = firstName.toLowerCase() + "@example.com";
        c.phone = "03-0000-000" + id;
        c.policyNumber = "ZT-2026-00" + id;
        c.policyType = "LIFE";
        c.policyStatus = 1;
        c.premiumAmount = 10000;
        c.agentName = agentName;
        c.agentEmail = agentName + "@example.co.jp";

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, daysUntilExpiry);
        c.policyEndDate = cal.getTime();
        return c;
    }

    @Test
    void getUpcomingRenewals_returnsMatchingCustomers() {
        Customer c1 = buildCustomer(1L, "花子", "鈴木", "佐藤花子", 10);
        Customer c2 = buildCustomer(2L, "一郎", "佐藤", "高橋次郎", 20);
        when(customerRepository.findUpcomingRenewals(any(Date.class), any(Date.class)))
                .thenReturn(List.of(c1, c2));

        Map<String, Object> result = renewalNotificationService.getUpcomingRenewals(30, null);

        assertEquals(2, result.get("totalCount"));
        assertEquals(30, result.get("daysBeforeExpiry"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> renewals = (List<Map<String, Object>>) result.get("renewals");
        assertEquals(2, renewals.size());
        assertEquals(1L, renewals.get(0).get("customerId"));
    }

    @Test
    void getUpcomingRenewals_returnsEmptyWhenNoMatches() {
        when(customerRepository.findUpcomingRenewals(any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        Map<String, Object> result = renewalNotificationService.getUpcomingRenewals(30, null);

        assertEquals(0, result.get("totalCount"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> renewals = (List<Map<String, Object>>) result.get("renewals");
        assertTrue(renewals.isEmpty());
    }

    @Test
    void getUpcomingRenewals_filtersByAgentName() {
        Customer c1 = buildCustomer(1L, "花子", "鈴木", "佐藤花子", 15);
        when(customerRepository.findUpcomingRenewalsByAgent(any(Date.class), any(Date.class), eq("佐藤花子")))
                .thenReturn(List.of(c1));

        Map<String, Object> result = renewalNotificationService.getUpcomingRenewals(30, "佐藤花子");

        assertEquals(1, result.get("totalCount"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> renewals = (List<Map<String, Object>>) result.get("renewals");
        assertEquals("佐藤花子", renewals.get(0).get("agentName"));
        verify(customerRepository).findUpcomingRenewalsByAgent(any(Date.class), any(Date.class), eq("佐藤花子"));
        verify(customerRepository, never()).findUpcomingRenewals(any(Date.class), any(Date.class));
    }

    @Test
    void getUpcomingRenewals_nullAgentNameUsesAllAgentsQuery() {
        when(customerRepository.findUpcomingRenewals(any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        renewalNotificationService.getUpcomingRenewals(30, null);

        verify(customerRepository).findUpcomingRenewals(any(Date.class), any(Date.class));
        verify(customerRepository, never()).findUpcomingRenewalsByAgent(any(), any(), any());
    }

    @Test
    void getUpcomingRenewals_emptyAgentNameUsesAllAgentsQuery() {
        when(customerRepository.findUpcomingRenewals(any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        renewalNotificationService.getUpcomingRenewals(30, "  ");

        verify(customerRepository).findUpcomingRenewals(any(Date.class), any(Date.class));
        verify(customerRepository, never()).findUpcomingRenewalsByAgent(any(), any(), any());
    }

    @Test
    void getUpcomingRenewals_calculatesDaysUntilExpiryCorrectly() {
        Customer c = buildCustomer(1L, "花子", "鈴木", "佐藤花子", 0); // 当日が更新日
        when(customerRepository.findUpcomingRenewals(any(Date.class), any(Date.class)))
                .thenReturn(List.of(c));

        Map<String, Object> result = renewalNotificationService.getUpcomingRenewals(30, null);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> renewals = (List<Map<String, Object>>) result.get("renewals");
        assertEquals(0, renewals.get(0).get("daysUntilExpiry"));
    }

    @Test
    void getUpcomingRenewals_calculatesDaysUntilExpiry_nextDay() {
        Customer c = buildCustomer(1L, "花子", "鈴木", "佐藤花子", 1); // 翌日が更新日
        when(customerRepository.findUpcomingRenewals(any(Date.class), any(Date.class)))
                .thenReturn(List.of(c));

        Map<String, Object> result = renewalNotificationService.getUpcomingRenewals(30, null);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> renewals = (List<Map<String, Object>>) result.get("renewals");
        assertEquals(1, renewals.get(0).get("daysUntilExpiry"));
    }

    @Test
    void getUpcomingRenewals_throwsWhenDaysBeforeExpiryIsZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> renewalNotificationService.getUpcomingRenewals(0, null));
        assertEquals("daysBeforeExpiry must be a positive integer", ex.getMessage());
    }

    @Test
    void getUpcomingRenewals_throwsWhenDaysBeforeExpiryIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> renewalNotificationService.getUpcomingRenewals(-1, null));
    }

    @Test
    void getUpcomingRenewals_throwsWhenDaysBeforeExpiryExceeds365() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> renewalNotificationService.getUpcomingRenewals(366, null));
        assertEquals("daysBeforeExpiry must not exceed 365", ex.getMessage());
    }

    @Test
    void getUpcomingRenewals_acceptsBoundaryValues() {
        when(customerRepository.findUpcomingRenewals(any(Date.class), any(Date.class)))
                .thenReturn(Collections.emptyList());

        // 境界値: 1 と 365 は正常
        assertDoesNotThrow(() -> renewalNotificationService.getUpcomingRenewals(1, null));
        assertDoesNotThrow(() -> renewalNotificationService.getUpcomingRenewals(365, null));
    }
}
