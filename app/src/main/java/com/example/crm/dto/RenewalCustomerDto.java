package com.example.crm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 契約更新対象顧客1件分のレスポンスDTO
 */
public class RenewalCustomerDto {

    public Long customerId;
    public String firstName;
    public String lastName;
    public String fullName;
    public String email;
    public String phone;
    public String policyNumber;
    public String policyType;
    public int policyStatus;
    public String policyStatusText;
    public double premiumAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date policyStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date policyEndDate;

    public long daysUntilRenewal;
    public String agentName;
    public String agentEmail;

    public RenewalCustomerDto() {}
}
