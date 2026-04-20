package com.zurich.crm.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 顧客エンティティ
 * ※レガシーコードパターン: フィールドが多く責務が不明確、Date型の使用、publicフィールド
 */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String firstName;
    public String lastName;
    public String email;
    public String phone;
    public String address;

    // 保険関連フィールド（本来は別エンティティにすべき）
    public String policyNumber;
    public String policyType;  // "LIFE", "MEDICAL", "AUTO"
    public int policyStatus;   // 0=申請中, 1=有効, 2=失効, 3=解約
    public double premiumAmount;
    public Date policyStartDate;
    public Date policyEndDate;

    // 営業担当情報（本来は別テーブルで管理すべき）
    public String agentName;
    public String agentEmail;

    @Temporal(TemporalType.TIMESTAMP)
    public Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    public Date updatedAt;

    public Customer() {}

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // フルネーム取得（日本語対応なし）
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // ステータスを文字列で返す（マジックナンバー）
    public String getPolicyStatusText() {
        if (policyStatus == 0) return "申請中";
        if (policyStatus == 1) return "有効";
        if (policyStatus == 2) return "失効";
        if (policyStatus == 3) return "解約";
        return "不明";
    }
}
