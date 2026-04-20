package com.example.crm.service;

import com.example.crm.model.Customer;
import com.example.crm.model.Policy;
import com.example.crm.repository.CustomerRepository;
import com.example.crm.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 顧客管理サービス
 *
 * ※レガシーコードパターン:
 * - God Class（顧客管理・保険料計算・通知など複数の責務を1クラスに集約）
 * - マジックナンバーの多用
 * - エラーハンドリングの不備
 * - nullチェックの不足
 * - ビジネスロジックの散在
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PolicyRepository policyRepository;

    // 顧客一覧取得
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // 顧客詳細取得
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).get(); // NoSuchElementException の可能性
    }

    // 顧客登録（バリデーションなし）
    public Customer createCustomer(Customer customer) {
        customer.createdAt = new Date();
        customer.updatedAt = new Date();
        if (customer.policyStatus == 0) {
            customer.policyStatus = 0; // 冗長なコード
        }
        return customerRepository.save(customer);
    }

    // 顧客更新
    public Customer updateCustomer(Long id, Customer updated) {
        Customer existing = customerRepository.findById(id).get();
        existing.firstName = updated.firstName;
        existing.lastName = updated.lastName;
        existing.email = updated.email;
        existing.phone = updated.phone;
        existing.address = updated.address;
        existing.policyNumber = updated.policyNumber;
        existing.policyType = updated.policyType;
        existing.policyStatus = updated.policyStatus;
        existing.premiumAmount = updated.premiumAmount;
        existing.agentName = updated.agentName;
        existing.agentEmail = updated.agentEmail;
        existing.updatedAt = new Date();
        return customerRepository.save(existing);
    }

    // 顧客削除
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // 保険料計算（ハードコーディング、テスト困難）
    public double calculatePremium(String policyType, int age, boolean isSmoker) {
        double basePremium = 0;

        if (policyType.equals("LIFE")) {
            basePremium = 5000;
            if (age > 60) {
                basePremium = basePremium * 2.5;
            } else if (age > 40) {
                basePremium = basePremium * 1.8;
            } else if (age > 30) {
                basePremium = basePremium * 1.2;
            }
            if (isSmoker) {
                basePremium = basePremium * 1.5;
            }
        } else if (policyType.equals("MEDICAL")) {
            basePremium = 3000;
            if (age > 60) {
                basePremium = basePremium * 3.0;
            } else if (age > 40) {
                basePremium = basePremium * 2.0;
            } else if (age > 30) {
                basePremium = basePremium * 1.3;
            }
            if (isSmoker) {
                basePremium = basePremium * 1.8;
            }
        } else if (policyType.equals("AUTO")) {
            basePremium = 8000;
            if (age < 25) {
                basePremium = basePremium * 2.0;
            } else if (age > 65) {
                basePremium = basePremium * 1.5;
            }
        } else if (policyType.equals("FIRE")) {
            basePremium = 2000;
        }

        // 消費税加算（マジックナンバー）
        basePremium = basePremium * 1.10;

        return Math.round(basePremium);
    }

    // 契約ステータス変更（入力検証なし）
    public Customer changePolicyStatus(Long customerId, int newStatus) {
        Customer customer = customerRepository.findById(customerId).get();
        customer.policyStatus = newStatus;
        customer.updatedAt = new Date();
        return customerRepository.save(customer);
    }

    // 顧客検索（非効率な実装）
    public List<Customer> searchCustomers(String keyword) {
        List<Customer> allCustomers = customerRepository.findAll();
        List<Customer> results = new ArrayList<>();
        for (Customer c : allCustomers) {
            if (c.firstName != null && c.firstName.contains(keyword)) {
                results.add(c);
            } else if (c.lastName != null && c.lastName.contains(keyword)) {
                results.add(c);
            } else if (c.email != null && c.email.contains(keyword)) {
                results.add(c);
            } else if (c.policyNumber != null && c.policyNumber.contains(keyword)) {
                results.add(c);
            }
        }
        return results;
    }

    // 月次レポート生成（本来は別サービスに分離すべき）
    public Map<String, Object> generateMonthlyReport() {
        Map<String, Object> report = new HashMap<>();
        List<Customer> all = customerRepository.findAll();

        int totalCustomers = all.size();
        int activeCount = 0;
        int expiredCount = 0;
        double totalPremium = 0;

        for (Customer c : all) {
            if (c.policyStatus == 1) {
                activeCount++;
                totalPremium += c.premiumAmount;
            } else if (c.policyStatus == 2) {
                expiredCount++;
            }
        }

        report.put("totalCustomers", totalCustomers);
        report.put("activeContracts", activeCount);
        report.put("expiredContracts", expiredCount);
        report.put("totalMonthlyPremium", totalPremium);
        report.put("averagePremium", activeCount > 0 ? totalPremium / activeCount : 0);
        report.put("generatedAt", new Date().toString());

        return report;
    }

    // メール送信（ダミー、本来は別サービス）
    public boolean sendNotification(Long customerId, String message) {
        Customer customer = customerRepository.findById(customerId).get();
        if (customer.email == null || customer.email.isEmpty()) {
            return false;
        }
        // TODO: 実際のメール送信処理を実装
        System.out.println("メール送信: " + customer.email + " - " + message);
        return true;
    }
}
