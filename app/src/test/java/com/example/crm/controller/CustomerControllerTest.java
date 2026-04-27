package com.example.crm.controller;

import com.example.crm.model.Customer;
import com.example.crm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();

        // 更新間近（有効） — 15日後
        saveCustomer("花子", "鈴木", 1, LocalDate.now().plusDays(15), "佐藤花子");
        // 更新間近（有効） — 10日後
        saveCustomer("一郎", "佐藤", 1, LocalDate.now().plusDays(10), "高橋次郎");
        // 更新日が遠い — 60日後
        saveCustomer("美咲", "高橋", 1, LocalDate.now().plusDays(60), "佐藤花子");
        // 失効（ステータス2） — 5日後
        saveCustomer("健太", "渡辺", 2, LocalDate.now().plusDays(5), "佐藤花子");
        // 更新日がNULL
        saveCustomer("由美", "伊藤", 1, null, "佐藤花子");
        // 更新日が過去 — 5日前
        saveCustomer("大輔", "山本", 1, LocalDate.now().minusDays(5), "山田三郎");
    }

    private void saveCustomer(String firstName, String lastName, int status, LocalDate endDate, String agentName) {
        Customer c = new Customer(firstName, lastName, firstName.toLowerCase() + "@example.com");
        c.policyNumber = "POL-" + firstName;
        c.policyType = "LIFE";
        c.policyStatus = status;
        c.premiumAmount = 10000;
        c.policyStartDate = new Date();
        c.policyEndDate = endDate != null
                ? Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
                : null;
        c.agentName = agentName;
        c.agentEmail = agentName + "@example.co.jp";
        customerRepository.save(c);
    }

    @Test
    void getRenewals_default_returns200WithResults() throws Exception {
        mockMvc.perform(get("/api/customers/renewals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.renewals").isArray())
                .andExpect(jsonPath("$.totalCount").isNumber())
                .andExpect(jsonPath("$.searchCriteria").exists())
                // 30日以内: 10日後、15日後、-5日前 の3件（有効のみ、NULL除外、失効除外）
                .andExpect(jsonPath("$.totalCount").value(3));
    }

    @Test
    void getRenewals_customDays60_includesMoreResults() throws Exception {
        mockMvc.perform(get("/api/customers/renewals").param("daysUntilRenewal", "60"))
                .andExpect(status().isOk())
                // 60日以内: -5日前、10日後、15日後、60日後 の4件
                .andExpect(jsonPath("$.totalCount").value(4));
    }

    @Test
    void getRenewals_daysZero_returns400() throws Exception {
        mockMvc.perform(get("/api/customers/renewals").param("daysUntilRenewal", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("daysUntilRenewal must be a positive integer"));
    }

    @Test
    void getRenewals_daysExceeds365_returns400() throws Exception {
        mockMvc.perform(get("/api/customers/renewals").param("daysUntilRenewal", "400"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("daysUntilRenewal must not exceed 365"));
    }

    @Test
    void getRenewals_agentNameFilter_returnsOnlyMatchingAgent() throws Exception {
        mockMvc.perform(get("/api/customers/renewals")
                        .param("daysUntilRenewal", "60")
                        .param("agentName", "佐藤花子"))
                .andExpect(status().isOk())
                // 佐藤花子担当で有効: 花子鈴木(15日後)、美咲高橋(60日後) の2件
                .andExpect(jsonPath("$.totalCount").value(2))
                .andExpect(jsonPath("$.renewals[*].agentName", everyItem(is("佐藤花子"))));
    }

    @Test
    void getRenewals_nonExistentAgent_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/customers/renewals").param("agentName", "存在しない担当者"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(0))
                .andExpect(jsonPath("$.renewals").isEmpty());
    }

    @Test
    void getRenewals_partialAgentName_returnsMatchingResults() throws Exception {
        mockMvc.perform(get("/api/customers/renewals")
                        .param("daysUntilRenewal", "60")
                        .param("agentName", "佐藤"))
                .andExpect(status().isOk())
                // 「佐藤」で部分一致: 佐藤花子担当の花子鈴木(15日後)、美咲高橋(60日後) の2件
                .andExpect(jsonPath("$.totalCount").value(2))
                .andExpect(jsonPath("$.renewals[*].agentName", everyItem(containsString("佐藤"))));
    }

    @Test
    void getRenewals_excludesNonActiveStatus() throws Exception {
        // デフォルト30日で取得 — 失効(status=2)の渡辺健太(5日後)は含まれない
        mockMvc.perform(get("/api/customers/renewals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.renewals[*].policyStatus", everyItem(is(1))));
    }

    @Test
    void getRenewals_sortedByEndDateAsc() throws Exception {
        mockMvc.perform(get("/api/customers/renewals"))
                .andExpect(status().isOk())
                // 更新日が近い順: 大輔(-5日) → 一郎(10日) → 花子(15日)
                .andExpect(jsonPath("$.renewals[0].firstName").value("大輔"))
                .andExpect(jsonPath("$.renewals[1].firstName").value("一郎"))
                .andExpect(jsonPath("$.renewals[2].firstName").value("花子"));
    }
}
