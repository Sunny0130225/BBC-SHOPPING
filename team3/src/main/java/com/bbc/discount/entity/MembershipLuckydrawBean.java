package com.bbc.discount.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "membership_luckydraw")
public class MembershipLuckydrawBean {

    @Id
    private int id;  // 用戶ID

    private int count; // 剩餘次數

    public MembershipLuckydrawBean() {}

    public MembershipLuckydrawBean(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
