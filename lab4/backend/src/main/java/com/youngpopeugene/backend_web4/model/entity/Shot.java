package com.example.AreaChecker.model.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "shots")
public class Shot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;
    @Basic
    @Column(name = "x", nullable = false)
    private double x;
    @Basic
    @Column(name = "y", nullable = false)
    private double y;
    @Basic
    @Column(name = "r", nullable = false)
    private double r;
    @Basic
    @Column(name = "status", nullable = false)
    private boolean status;
    @Basic
    @Column(name = "currentTime", nullable = false)
    private Timestamp currentTime;
    @Basic
    @Column(name = "scriptTime", nullable = false)
    private long scriptTime;

    public Shot(){

    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Timestamp getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Timestamp currentTime) {
        this.currentTime = currentTime;
    }

    public long getScriptTime() {
        return scriptTime;
    }

    public void setScriptTime(long scriptTime) {
        this.scriptTime = scriptTime;
    }
}
