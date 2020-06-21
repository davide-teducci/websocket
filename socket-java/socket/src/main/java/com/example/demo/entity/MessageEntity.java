package com.example.demo.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageEntity {
	
	private String value;
	
	private String sendDate;
	
	public MessageEntity(String value) {
		this.value = value;
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		this.sendDate = df.format(new Date());
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSendDate() {
		return sendDate;
	}

	@Override
	public String toString() {
		return "MessageEntity [value=" + value + ", sendDate=" + sendDate + "]";
	}
	
}
