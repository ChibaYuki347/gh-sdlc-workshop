package com.example.crm.controller;

import com.example.crm.model.Customer;
import com.example.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 顧客管理API コントローラ
 *
 * ※レガシーコードパターン:
 * - 例外処理をコントローラで行っていない
 * - レスポンス形式が統一されていない
 * - 入力バリデーションの欠如
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id); // 404処理なし
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer); // バリデーションなし
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    // 保険料試算API
    @GetMapping("/premium/calculate")
    public ResponseEntity<?> calculatePremium(
            @RequestParam String policyType,
            @RequestParam int age,
            @RequestParam(defaultValue = "false") boolean isSmoker) {
        double premium = customerService.calculatePremium(policyType, age, isSmoker);
        return ResponseEntity.ok(Map.of(
            "policyType", policyType,
            "age", age,
            "isSmoker", isSmoker,
            "monthlyPremium", premium
        ));
    }

    // ステータス変更API
    @PutMapping("/{id}/status")
    public Customer changePolicyStatus(
            @PathVariable Long id,
            @RequestParam int status) {
        return customerService.changePolicyStatus(id, status);
    }

    // 顧客検索API
    @GetMapping("/search")
    public List<Customer> searchCustomers(@RequestParam String keyword) {
        return customerService.searchCustomers(keyword);
    }

    // 月次レポートAPI
    @GetMapping("/report/monthly")
    public Map<String, Object> getMonthlyReport() {
        return customerService.generateMonthlyReport();
    }
}
