

package com.example.AreaChecker.endpoint;

import jakarta.servlet.ServletConfig;
import jakarta.xml.ws.Endpoint;
import com.example.AreaChecker.controller.PingImpl;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet(urlPatterns = "/ws/ping", loadOnStartup = 1)
public class PingPublisher extends HttpServlet {
	public void init(ServletConfig config) {
		Endpoint.publish("http://0.0.0.0:9999/ws/ping", new PingImpl());
	}
}