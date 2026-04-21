package com.example.crm.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RenewalNotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRenewals_defaultParams_returns200WithCorrectStructure() throws Exception {
        mockMvc.perform(get("/api/notifications/renewals")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalCount").isNumber())
                .andExpect(jsonPath("$.daysBeforeExpiry").value(30))
                .andExpect(jsonPath("$.renewals").isArray());
    }

    @Test
    void getRenewals_defaultParams_returnsOnlyActiveContractsWithinWindow() throws Exception {
        // デフォルト30日以内: 鈴木（15日後）と佐藤（25日後）が対象
        mockMvc.perform(get("/api/notifications/renewals")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(2))
                .andExpect(jsonPath("$.renewals", hasSize(2)));
    }

    @Test
    void getRenewals_withDaysBeforeExpiry60_includesMoreResults() throws Exception {
        // 60日以内: 鈴木（15日後）、佐藤（25日後）、伊藤（50日後）が対象
        mockMvc.perform(get("/api/notifications/renewals")
                .param("daysBeforeExpiry", "60")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.daysBeforeExpiry").value(60))
                .andExpect(jsonPath("$.totalCount").value(3))
                .andExpect(jsonPath("$.renewals", hasSize(3)));
    }

    @Test
    void getRenewals_withAgentNameFilter_returnsOnlyMatchingAgent() throws Exception {
        // 佐藤花子: 鈴木（15日後）のみ（伊藤は30日超なので含まれない）
        mockMvc.perform(get("/api/notifications/renewals")
                .param("agentName", "佐藤花子")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.renewals[0].agentName").value("佐藤花子"));
    }

    @Test
    void getRenewals_withAgentNameFilterAnd60Days_returnsCorrectResults() throws Exception {
        // 佐藤花子 + 60日以内: 鈴木（15日後）と伊藤（50日後）
        mockMvc.perform(get("/api/notifications/renewals")
                .param("daysBeforeExpiry", "60")
                .param("agentName", "佐藤花子")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(2));
    }

    @Test
    void getRenewals_sortedByPolicyEndDateAsc() throws Exception {
        // 30日以内: 鈴木（15日後）→ 佐藤（25日後）の昇順を確認
        mockMvc.perform(get("/api/notifications/renewals")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.renewals[0].lastName").value("鈴木"))
                .andExpect(jsonPath("$.renewals[1].lastName").value("佐藤"));
    }

    @Test
    void getRenewals_responseContainsDaysUntilExpiry() throws Exception {
        mockMvc.perform(get("/api/notifications/renewals")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.renewals[0].daysUntilExpiry").isNumber());
    }

    @Test
    void getRenewals_daysBeforeExpiryZero_returns400() throws Exception {
        mockMvc.perform(get("/api/notifications/renewals")
                .param("daysBeforeExpiry", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("daysBeforeExpiry must be a positive integer"));
    }

    @Test
    void getRenewals_daysBeforeExpiryNegative_returns400() throws Exception {
        mockMvc.perform(get("/api/notifications/renewals")
                .param("daysBeforeExpiry", "-1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString());
    }

    @Test
    void getRenewals_daysBeforeExpiry500_returns400() throws Exception {
        mockMvc.perform(get("/api/notifications/renewals")
                .param("daysBeforeExpiry", "500")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("daysBeforeExpiry must not exceed 365"));
    }

    @Test
    void getRenewals_unknownAgentName_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/notifications/renewals")
                .param("agentName", "存在しない担当者")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(0))
                .andExpect(jsonPath("$.renewals").isEmpty());
    }

    @Test
    void existingCustomersEndpoint_isUnaffected() throws Exception {
        mockMvc.perform(get("/api/customers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
