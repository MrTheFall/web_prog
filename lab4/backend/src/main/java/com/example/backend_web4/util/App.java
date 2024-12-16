package com.example.AreaChecker.util;

import com.example.AreaChecker.controller.LoginController;
import com.example.AreaChecker.controller.RegistrationController;
import com.example.AreaChecker.controller.ShotController;

import com.example.AreaChecker.controller.UserController;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class App extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(ShotController.class);
        set.add(UserController.class);
        set.add(RegistrationController.class);
        set.add(LoginController.class);
        return set;
    }
}
