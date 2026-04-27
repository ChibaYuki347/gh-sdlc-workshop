package com.example.crm.dto;

import java.util.List;
import java.util.Map;

/**
 * 契約更新通知APIのレスポンス全体を表すDTO
 */
public class RenewalNotificationResponse {

    public List<RenewalCustomerDto> renewals;
    public int totalCount;
    public Map<String, Object> searchCriteria;

    public RenewalNotificationResponse() {}

    public RenewalNotificationResponse(List<RenewalCustomerDto> renewals, int daysUntilRenewal, String agentName) {
        this.renewals = renewals;
        this.totalCount = renewals.size();
        this.searchCriteria = Map.of(
            "daysUntilRenewal", daysUntilRenewal,
            "agentName", agentName != null ? agentName : "null"
        );
    }
}
