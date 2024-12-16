package com.example.AreaChecker.controller;

import com.example.AreaChecker.util.AuthUtil;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Path("/auth/login")
public class LoginController {
    @EJB
    private GlobalBean globalBean;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authorize(UserLoginData loginData) {
        boolean isRegistered = globalBean.isRegistered(loginData.getUsername(),
                loginData.getPassword());

        if (isRegistered) {
            String jwtToken = AuthUtil.createJwtToken(loginData.getUsername());
            NewCookie jwtCookie = new NewCookie("jwt", jwtToken, "/", null, null, -1, false);

            return makeResponse(Response.ok()
                    .entity("{\"success\": true, \"message\": \"Authorization is succeeded\"}")
                    .cookie(jwtCookie));
        } else {
            return makeResponse(Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"success\": false, \"message\": \"Login or password is incorrect\"}"));
        }
    }

    public Response makeResponse(Response.ResponseBuilder responseBuilder) {
        return responseBuilder.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }

    public static class UserLoginData {
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
