package com.example.crm.service;

import com.example.crm.model.Customer;
import com.example.crm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 契約更新通知サービス
 * 更新日が近い保険契約をリストアップするビジネスロジックを提供する。
 */
@Service
public class RenewalNotificationService {

    private static final long MILLIS_PER_DAY = 86400000L;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * 更新日が指定日数以内に迫っている有効契約の顧客一覧を取得する。
     *
     * @param daysBeforeExpiry 更新日までの日数しきい値（1〜365）
     * @param agentName        営業担当者名でフィルタ（null または空文字の場合は全担当者対象）
     * @return totalCount, daysBeforeExpiry, renewals を含む Map
     * @throws IllegalArgumentException daysBeforeExpiry が 1〜365 の範囲外の場合
     */
    public Map<String, Object> getUpcomingRenewals(int daysBeforeExpiry, String agentName) {
        if (daysBeforeExpiry <= 0) {
            throw new IllegalArgumentException("daysBeforeExpiry must be a positive integer");
        }
        if (daysBeforeExpiry > 365) {
            throw new IllegalArgumentException("daysBeforeExpiry must not exceed 365");
        }

        // 今日の日付（時刻成分をゼロにする）
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, daysBeforeExpiry);
        Date expiryDate = cal.getTime();

        List<Customer> customers;
        if (agentName == null || agentName.trim().isEmpty()) {
            customers = customerRepository.findUpcomingRenewals(today, expiryDate);
        } else {
            customers = customerRepository.findUpcomingRenewalsByAgent(today, expiryDate, agentName);
        }

        List<Map<String, Object>> renewals = new ArrayList<>();
        for (Customer c : customers) {
            Map<String, Object> item = new HashMap<>();
            item.put("customerId", c.id);
            item.put("firstName", c.firstName);
            item.put("lastName", c.lastName);
            item.put("email", c.email);
            item.put("phone", c.phone);
            item.put("policyNumber", c.policyNumber);
            item.put("policyType", c.policyType);
            item.put("premiumAmount", c.premiumAmount);
            item.put("policyEndDate", c.policyEndDate);
            item.put("agentName", c.agentName);
            item.put("agentEmail", c.agentEmail);

            // 残日数を計算（当日=0日）
            long diffMs = c.policyEndDate.getTime() - today.getTime();
            long daysUntilExpiry = diffMs / MILLIS_PER_DAY;
            item.put("daysUntilExpiry", (int) daysUntilExpiry);

            renewals.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", renewals.size());
        result.put("daysBeforeExpiry", daysBeforeExpiry);
        result.put("renewals", renewals);

        return result;
    }
}
