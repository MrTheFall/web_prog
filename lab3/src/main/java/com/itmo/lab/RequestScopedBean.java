package com.itmo.lab;

import com.itmo.lab.models.CheckResult;
import com.itmo.lab.utils.AreaCheckUtils;
import com.itmo.lab.utils.DatabaseUtils;
import jakarta.enterprise.context.RequestScoped;
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
@RequestScoped
public class RequestScopedBean implements Serializable {
    private int x;

    public int getX() { x +=1; return x; }
}
