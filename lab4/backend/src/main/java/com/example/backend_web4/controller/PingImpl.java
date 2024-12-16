package com.example.AreaChecker.controller;
 
import jakarta.jws.WebService;
 
@WebService(endpointInterface = "com.example.AreaChecker.controller.Ping")
public class PingImpl implements Ping{

	@Override
	public String ping(String name) {
		return "PONG: " + name;
	}

}