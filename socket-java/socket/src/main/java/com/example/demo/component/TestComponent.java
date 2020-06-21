package com.example.demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.entity.MessageEntity;
import com.example.demo.services.SocketNotifierService;

@Component
public class TestComponent implements CommandLineRunner{
	
	@Autowired
	SocketNotifierService socketNotifierService;
	
	@Override
	public void run(String... args) throws Exception {
		for(int i = 1; i< 1000; i++) {
			MessageEntity response = new MessageEntity("Questo è il messaggio Num: " + i);
			socketNotifierService.sendTaskMonitorStatus(response);		
			Thread.sleep(1000);
		}
	}
}
