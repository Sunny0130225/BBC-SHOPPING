package com.bbc.discount.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "luckydraw")
public class LuckyDrawBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LDid;
    private String LDname;
    private Integer LDmax;

    // Getters and Setters
    public Long getLDid() {
        return LDid;
    }

    public void setLDid(Long LDid) {
        this.LDid = LDid;
    }

    public String getLDname() {
        return LDname;
    }

    public void setLDname(String LDname) {
        this.LDname = LDname;
    }

    public Integer getLDmax() {
        return LDmax;
    }

    public void setLDmax(Integer LDmax) {
        this.LDmax = LDmax;
    }
}