package com.example.crm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
            "application", "Insurance CRM API",
            "status", "running",
            "endpoints", Map.of(
                "customers", "/api/customers",
                "customerById", "/api/customers/{id}",
                "search", "/api/customers/search?keyword=xxx",
                "premiumCalc", "/api/customers/premium/calculate?policyType=LIFE&age=30",
                "monthlyReport", "/api/customers/report/monthly"
            )
        );
    }
}
