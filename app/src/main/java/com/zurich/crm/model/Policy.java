package com.zurich.crm.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 保険契約エンティティ
 */
@Entity
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long customerId;
    public String policyNumber;
    public String type;         // LIFE, MEDICAL, AUTO, FIRE
    public int status;          // 0=申請中, 1=有効, 2=失効, 3=解約
    public double premiumAmount;
    public double coverageAmount;

    @Temporal(TemporalType.DATE)
    public Date startDate;

    @Temporal(TemporalType.DATE)
    public Date endDate;

    public String notes;

    @Temporal(TemporalType.TIMESTAMP)
    public Date createdAt;

    public Policy() {}
}
