package com.example.crm.controller;

import com.example.crm.service.RenewalNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 契約更新通知コントローラ
 * GET /api/notifications/renewals エンドポイントを提供する。
 */
@RestController
@RequestMapping("/api/notifications")
public class RenewalNotificationController {

    @Autowired
    private RenewalNotificationService renewalNotificationService;

    /**
     * 更新日が近い顧客一覧を返す。
     *
     * @param daysBeforeExpiry 更新日までの日数しきい値（デフォルト: 30）
     * @param agentName        営業担当者名でフィルタ（任意）
     * @return 200 OK と更新対象顧客リスト、または 400 Bad Request
     */
    @GetMapping("/renewals")
    public ResponseEntity<?> getUpcomingRenewals(
            @RequestParam(defaultValue = "30") int daysBeforeExpiry,
            @RequestParam(required = false) String agentName) {
        try {
            Map<String, Object> result = renewalNotificationService.getUpcomingRenewals(daysBeforeExpiry, agentName);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
