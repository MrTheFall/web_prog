package com.itmo.lab;

import com.itmo.lab.models.CheckResult;
import com.itmo.lab.utils.AreaCheckUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaCheckServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        getServletContext().setAttribute("userResults", new HashMap<String, List<CheckResult>>());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*if (!Boolean.TRUE.equals(request.getAttribute("forwardedFromController"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }*/

        String sessionId = request.getSession().getId();
        Map<String, List<CheckResult>> userResults = getUserResults();

        long startTime = System.currentTimeMillis();
        String xParam = request.getParameter("x");
        String yParam = request.getParameter("y");
        String rParam = request.getParameter("R");

        if ((xParam == null || yParam == null || rParam == null) && !(xParam == null && yParam == null && rParam == null)) {
            handleMissingParameters(request, response, sessionId, xParam, yParam, rParam, userResults);
        } else {
            handleParameterProcessing(request, response, sessionId, userResults, startTime, xParam, yParam, rParam);
        }
    }

    private Map<String, List<CheckResult>> getUserResults() {
        return (Map<String, List<CheckResult>>) getServletContext().getAttribute("userResults");
    }

    private void handleParameterProcessing(HttpServletRequest request, HttpServletResponse response,
                                           String sessionId, Map<String, List<CheckResult>> userResults,
                                           long startTime, String xParam, String yParam, String rParam) throws ServletException, IOException {
        if (xParam == null && yParam == null && rParam == null)
        {
            setRequestAttributes(request, sessionId, userResults, null);
            forwardToIndex(request, response);
            return;
        }
        try {
            int x = Integer.parseInt(xParam);
            double y = Double.parseDouble(yParam);
            int r = Integer.parseInt(rParam);

            String precisionError = AreaCheckUtils.validatePrecision(yParam, y);
            if (precisionError != null || !AreaCheckUtils.isValidValues(x, y, r)) {
                setRequestAttributes(request, sessionId, userResults, precisionError != null ? precisionError : "Invalid values for x, y, or R.");
                forwardToIndex(request, response);
                return;
            }

            boolean hit = AreaCheckUtils.checkArea(x, y, r);
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            long executionTime = System.currentTimeMillis() - startTime;

            CheckResult result = new CheckResult(x, y, r, hit, time, executionTime);
            List<CheckResult> results = userResults.computeIfAbsent(sessionId, k -> new ArrayList<>());
            if (results.size() > 0){
                result.setId(results.get(results.size() - 1).getId() + 1);
            }
            results.add(result);

            // Limit the size of the results list to MAX_HITS_PER_USER
            if (results.size() > 10) {
                results.remove(0); // Remove the oldest hit
            }

            setRequestAttributes(request, sessionId, userResults, null);
            forwardToIndex(request, response);
        } catch (NumberFormatException e) {
            setRequestAttributes(request, sessionId, userResults, "Invalid number format for x, y, or R");
            forwardToIndex(request, response);
        }
    }

    private void handleMissingParameters(HttpServletRequest request, HttpServletResponse response,
                                         String sessionId, String xParam, String yParam, String rParam,
                                         Map<String, List<CheckResult>> userResults) throws ServletException, IOException {
        if (!(xParam == null && yParam == null && rParam == null)) {
            request.setAttribute("error", "One of the values is missing");
        }
        request.setAttribute("results", userResults.get(sessionId));
        forwardToIndex(request, response);
    }

    private void setRequestAttributes(HttpServletRequest request, String sessionId, Map<String, List<CheckResult>> userResults, String error) {
        request.setAttribute("results", userResults.get(sessionId));
        if (error != null) {
            request.setAttribute("error", error);
        }
    }

    private void forwardToIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
