package com.example.AreaChecker.controller;

import com.example.AreaChecker.util.JSONParser;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserController {
    @EJB
    private GlobalBean globalBean;

    @GET
    @Produces("plain/json")
    public Response getUsers() {
        return makeResponse(Response.ok().entity(JSONParser.usersToJSON(globalBean.getUsers())));
    }

    public Response makeResponse(Response.ResponseBuilder x){
        return x.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .build();
    }
}
