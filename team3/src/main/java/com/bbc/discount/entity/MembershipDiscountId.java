package com.bbc.discount.entity;

import java.io.Serializable;
import java.util.Objects;

public class MembershipDiscountId implements Serializable {

    private int id; // 會員 ID
    private int did; // 折價券 ID

    // 無參數構造函數（必要）
    public MembershipDiscountId() {}

    // 帶參數構造函數
    public MembershipDiscountId(int id, int did) {
        this.id = id;
        this.did = did;
    }

    // equals 和 hashCode 方法（必須覆寫，用於主鍵比較）
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MembershipDiscountId that = (MembershipDiscountId) o;
        return id == that.id && did == that.did;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, did);
    }
}