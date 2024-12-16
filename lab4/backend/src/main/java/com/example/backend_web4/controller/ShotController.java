package com.example.AreaChecker.controller;
import com.example.AreaChecker.util.ShotValidator;

import com.example.AreaChecker.model.entity.Shot;
import com.example.AreaChecker.util.AuthUtil;
import com.example.AreaChecker.util.JSONParser;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.example.AreaChecker.model.dao.UserDao;
import com.example.AreaChecker.model.entity.User;


import java.util.ArrayList;

@Path("/points")
public class ShotController {
    @EJB
    private GlobalBean globalBean;
    @EJB
    private UserDao userDao;

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addShot(ShotData data, @CookieParam("jwt") Cookie jwtCookie) {
        String login = AuthUtil.extractUsernameFromJwt(jwtCookie.getValue());
        if (login == null) {
            return makeResponse(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Invalid JWT token")
            );
        }

        ArrayList<Shot> result = new ArrayList<>();
        Shot shot = new Shot();
        
        try {
            shot.setX(data.getX());
            shot.setY(data.getY());
            shot.setR(data.getR());
        } catch (NumberFormatException e) {
            return makeResponse(Response
                    .serverError()
                    .entity("Error: x, y, r have to be a number")
            );
        }
        boolean isHit = ShotValidator.isHit(shot.getX(), shot.getY(), shot.getR());
        shot.setStatus(isHit);

        globalBean.addShot(shot, login);
        result.add(shot);

        // Manually construct JSON response
        String jsonResponse = String.format(
            "{\"id\": \"%s\", \"X\": \"%s\", \"Y\": \"%s\", \"R\": \"%s\", \"script_time\": \"%s\", \"date\": \"%s\", \"status\": \"%s\"}",
            String.valueOf(shot.getId()),
            String.valueOf(shot.getX()),
            String.valueOf(shot.getY()),
            String.valueOf(shot.getR()),
            String.valueOf(shot.getScriptTime()),  // Replace with the appropriate script time if needed
            String.valueOf(shot.getCurrentTime()),    // You can generate this dynamically if necessary
            String.valueOf(isHit)  // Set this based on your application logic
        );

        return makeResponse(Response
                .ok()
                .entity(jsonResponse)
                .type(MediaType.APPLICATION_JSON) // Set the content type
        );
    }

    @GET
    @Path("/get")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getShots(@CookieParam("jwt") Cookie jwtCookie) {
        User user = userDao.getUserByUsername(AuthUtil.extractUsernameFromJwt(jwtCookie.getValue()));
        if (user == null) {
            return makeResponse(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Invalid JWT token")
            );
        }

        return makeResponse(Response
                .ok()
                .entity(JSONParser.toJSON(globalBean.getShots(user.getUsername())))
        );
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteShots(@CookieParam("jwt") Cookie jwtCookie) {
        User user = userDao.getUserByUsername(AuthUtil.extractUsernameFromJwt(jwtCookie.getValue()));
        if (user == null) {
            return makeResponse(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity("Invalid JWT token")
            );
        }

        globalBean.deleteShots(user.getUsername());
        return makeResponse(Response
                .ok()
                .entity("All shots are deleted")
        );
    }

    public Response makeResponse(Response.ResponseBuilder x) {
        return x.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }


    public static class ShotData {
        private double x;
        private double y;
        private double r;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getR() {
            return r;
        }

        public void setR(double r) {
            this.r = r;
        }
    }
}

