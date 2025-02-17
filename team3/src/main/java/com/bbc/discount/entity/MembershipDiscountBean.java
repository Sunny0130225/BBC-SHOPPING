package com.bbc.discount.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(MembershipDiscountId.class) // 指定複合主鍵類
@Table(name = "membership_discount")
public class MembershipDiscountBean {

    @Id
    private int id; // 會員 ID

    @Id
    private int did; // 折價券 ID

    @Column(nullable = false)
    private int remaining; // 剩餘次數

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }
}