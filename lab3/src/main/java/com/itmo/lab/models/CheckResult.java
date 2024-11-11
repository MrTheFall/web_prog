package com.itmo.lab.models;

public class CheckResult {
    private int id;
    private final double y;
    private final int x, r;
    private final boolean hit;
    private final String time;
    private final long executionTime;

    public CheckResult(int id, int x, double y, int r, boolean hit, String time, long executionTime) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.time = time;
        this.executionTime = executionTime;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getX() { return x; }
    public double getY() { return y; }
    public int getR() { return r; }
    public boolean isHit() { return hit; }
    public String getTime() { return time; }
    public long getExecutionTime() { return executionTime; }
}
