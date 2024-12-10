package com.example.AreaChecker.controller;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;
 
@WebService
@SOAPBinding(style = Style.RPC)
public interface Ping{
 
	@WebMethod String ping(String name);

}