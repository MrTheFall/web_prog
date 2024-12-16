package com.example.AreaChecker.controller;

import com.example.AreaChecker.util.AuthUtil;
import com.example.AreaChecker.model.entity.User;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@Path("/auth/register")
public class RegistrationController {
    @EJB
    private GlobalBean globalBean;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registration(UserRegistrationData registrationData) {
        User user = new User();
        user.setUsername(registrationData.getUsername());
        //user.setPassword(Hashing.sha256().hashString(registrationData.getPassword(), StandardCharsets.UTF_8).toString());
        try {
            user.setPassword(AuthUtil.hashPassword(registrationData.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Hashing algorithm not found")
                    .build();
        }

        if (globalBean.isUsernameExists(registrationData.getUsername())) {
            return makeResponse(Response.status(Response.Status.FORBIDDEN).entity("{\"success\": false, \"message\":\"User already exists\"}"));
        }

        globalBean.addUser(user);
        return makeResponse(Response.ok().entity("{\"success\": true, \"message\":\"User created\"}"));
    }

    public Response makeResponse(Response.ResponseBuilder responseBuilder) {
        return responseBuilder.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

    public static class UserRegistrationData {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}