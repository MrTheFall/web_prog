package com.example.AreaChecker.util;

import com.example.AreaChecker.controller.GlobalBean;
import jakarta.ejb.EJB;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFilter extends HttpFilter{

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if(AuthUtil.parseAuth(request)) {
            chain.doFilter(request, response);
        }else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

}
