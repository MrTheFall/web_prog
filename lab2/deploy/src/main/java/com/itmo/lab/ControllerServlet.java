package com.itmo.lab;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class ControllerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String R = request.getParameter("R");
        HttpSession session = request.getSession(false); // Force disable creating new session, to check if user has one

        if ((x != null && y != null && R != null) || session != null ) {
            request.setAttribute("forwardedFromController", true);
            request.getRequestDispatcher("/AreaCheckServlet").forward(request, response);
        } else {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}
