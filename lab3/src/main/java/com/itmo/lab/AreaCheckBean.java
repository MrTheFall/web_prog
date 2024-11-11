package com.itmo.lab;

import com.itmo.lab.models.CheckResult;
import com.itmo.lab.utils.AreaCheckUtils;
import com.itmo.lab.utils.DatabaseUtils;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Named
@SessionScoped
public class AreaCheckBean implements Serializable {
    private int x;
    private double y;
    private int r;
    private List<CheckResult> results;
    private String sessionId;

    // Allowed X and R values
    private final int[] xValues = {-4, -3, -2, -1, 0, 1, 2, 3, 4};
    private final int[] rValues = {1, 2, 3, 4, 5};

    @Inject
    private HttpServletRequest request;

    @PostConstruct
    public void init() {
        HttpSession session = request.getSession();
        sessionId = session.getId();
        refreshResults();
    }

    public void checkPoint() {
        long startTime = System.currentTimeMillis();
        boolean hit = AreaCheckUtils.checkArea(x, y, r);
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        long executionTime = System.currentTimeMillis() - startTime;

        CheckResult result = new CheckResult(-1, x, y, r, hit, time, executionTime);
        DatabaseUtils.saveCheckResult(result, sessionId);  // Save result to the database
        refreshResults();
    }

    private void refreshResults() {
        results = DatabaseUtils.getLast10CheckResults(sessionId);  // Retrieve last 10 results
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public int getR() { return r; }
    public void setR(int r) { this.r = r; }
    public int[] getxValues() { return xValues; }
    public int[] getrValues() { return rValues; }
    public List<CheckResult> getResults() { return results; }
}
