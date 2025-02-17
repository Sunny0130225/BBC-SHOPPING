package com.bbc.discount.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "discount")
public class DiscountBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Did; // id
    private String Dname; // 名稱
    private String Dtype; // 折扣類型
    private int Dpercent; // 折扣
    private String Ddepart; // 限制
    private LocalDateTime Dts; // 開始時間
    private LocalDateTime Dte; // 結束時間

    // Getters and Setters
    public int getDid() { return Did; }
    public void setDid(int did) { Did = did; }
    public String getDname() { return Dname; }
    public void setDname(String dname) { Dname = dname; }
    public String getDtype() { return Dtype; }
    public void setDtype(String dtype) { Dtype = dtype; }
    public int getDpercent() { return Dpercent; }
    public void setDpercent(int dpercent) { Dpercent = dpercent; }
    public String getDdepart() { return Ddepart; }
    public void setDdepart(String ddepart) { this.Ddepart = ddepart; }
    public LocalDateTime getDts() { return Dts; }
    public void setDts(LocalDateTime dts) { this.Dts = dts; }
    public LocalDateTime getDte() { return Dte; }
    public void setDte(LocalDateTime dte) { this.Dte = dte; }

    @Override
    public String toString() {
        return "DiscountBean{" +
                "Did=" + Did +
                ", Dname='" + Dname + '\'' +
                ", Dtype='" + Dtype + '\'' +
                ", Dpercent=" + Dpercent +
                ", Ddepart='" + Ddepart + '\'' +
                ", Dts=" + Dts +
                ", Dte=" + Dte +
                '}';
    }
}
