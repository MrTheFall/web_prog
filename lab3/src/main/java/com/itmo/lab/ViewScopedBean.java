package com.itmo.lab;

import com.itmo.lab.models.CheckResult;
import com.itmo.lab.utils.AreaCheckUtils;
import com.itmo.lab.utils.DatabaseUtils;
import jakarta.faces.view.ViewScoped;
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
@ViewScoped
public class ViewScopedBean implements Serializable {
    private int x;

    public int getX() { return x; }

    public void inc() { x+=1; }
}
