package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {
	
	@MessageMapping("/message")
	  @SendTo("/topic/socket")
	  public String greeting(String message) throws Exception {
	      Thread.sleep(1000);
	      return new String("Hello, " + message + "!");
	  }
}
