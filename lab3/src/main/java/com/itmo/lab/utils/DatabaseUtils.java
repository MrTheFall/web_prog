package com.itmo.lab.utils;

import com.itmo.lab.models.CheckResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {

    private static final String URL = "jdbc:postgresql://postgres:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void saveCheckResult(CheckResult result, String sessionId) {
        String sql = "INSERT INTO check_results (session_id, x, y, r, hit, check_time, execution_time) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessionId);
            stmt.setInt(2, result.getX());
            stmt.setDouble(3, result.getY());
            stmt.setInt(4, result.getR());
            stmt.setBoolean(5, result.isHit());
            stmt.setString(6, result.getTime());
            stmt.setLong(7, result.getExecutionTime());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving check result: " + e.getMessage());
        }
    }

    public static List<CheckResult> getLast10CheckResults(String sessionId) {
        List<CheckResult> results = new ArrayList<>();
        String sql = "SELECT id, x, y, r, hit, check_time, execution_time FROM (SELECT id, x, y, r, hit, check_time, execution_time FROM check_results WHERE session_id = ? ORDER BY id DESC LIMIT 10) AS subquery ORDER BY id ASC;";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessionId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int x = rs.getInt("x");
                double y = rs.getDouble("y");
                int r = rs.getInt("r");
                boolean hit = rs.getBoolean("hit");
                String checkTime = rs.getString("check_time");
                long executionTime = rs.getLong("execution_time");

                results.add(new CheckResult(id, x, y, r, hit, checkTime, executionTime));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving check results: " + e.getMessage());
        }
        return results;
    }
}
