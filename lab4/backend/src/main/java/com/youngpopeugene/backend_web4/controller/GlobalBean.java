package com.example.AreaChecker.controller;

import com.example.AreaChecker.model.dao.ShotDao;
import com.example.AreaChecker.model.dao.UserDao;
import com.example.AreaChecker.model.entity.Shot;
import com.example.AreaChecker.model.entity.User;
//import com.example.AreaChecker.util.ShotValidator;
import com.example.AreaChecker.util.AuthUtil;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.security.NoSuchAlgorithmException;

@Singleton
public class GlobalBean{
    @EJB
    private ShotDao shotDao;
    @EJB
    private UserDao userDao;

    public void addShot(Shot shot, String username){
        LocalDateTime startTime = LocalDateTime.now(ZoneOffset.UTC);
        //shot.setStatus(ShotValidator.isHit(shot.getX(), shot.getY(), shot.getR()));
        shot.setCurrentTime(new Timestamp(new Date().getTime()));
        shot.setUser(userDao.getUserByUsername(username));
        shot.setScriptTime(Math.round(LocalDateTime.now().minusNanos(startTime.getNano()).getNano() * 0.001));
        shotDao.addShot(shot);
    }

    public ArrayList<Shot> getShots(String username){
        return (ArrayList<Shot>) shotDao.getShots(userDao.getUserByUsername(username));
    }

    public void deleteShots(String username){
        shotDao.deleteShots(userDao.getUserByUsername(username));
    }

    public void addUser(User user){
        userDao.addUser(user);
    }

    public ArrayList<String> getUsers(){
        return userDao.getUsers();
    }

    public boolean isRegistered(String username, String password) {
        //String hashPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        String hashPassword = "";
        try {
            hashPassword = AuthUtil.hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No hashing alg found", e);
        }
        User user = userDao.getUserByUsername(username);
        return user != null && user.getPassword().equals(hashPassword);
    }

    public boolean isUsernameExists(String username){
        return getUsers().contains(username);
    }

}
