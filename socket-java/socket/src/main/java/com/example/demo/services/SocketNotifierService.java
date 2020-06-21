package com.example.demo.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SocketNotifierService implements DisposableBean{

	private WebSocketStompClient stompClient;
	private ListenableFuture<StompSession> stompSession;
	
	@Value("${application.host}")	
	private String host;
	
	@Value("${application.port}")
	private Integer port;
	
	public void sendTaskMonitorStatus(Object obj) {
		try {
			if (stompSession == null)
				this.connect();
			if (!stompSession.get().isConnected())
				return;
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(obj);
			stompSession.get().send("/message", jsonString.getBytes());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private synchronized void connect() throws Exception {
		if (this.stompClient != null)
			return;
		Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		List<Transport> transports = Collections.singletonList(webSocketTransport);
		SockJsClient sockJsClient = new SockJsClient(transports);
		sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
		this.stompClient = new WebSocketStompClient(sockJsClient);
		String url = "ws://{host}:{port}/websocket";
		this.stompSession = stompClient.connect(url, new WebSocketHttpHeaders(), new StompSessionHandlerAdapter() {
			public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
			}
		}, host, port);
	}
	
	@Override
	public void destroy() throws Exception {
		if (this.stompClient != null && this.stompClient.isRunning())
			this.stompClient.stop();
		if (this.stompSession != null && this.stompSession.get() != null && this.stompSession.get().isConnected())
			this.stompSession.get().disconnect();
	}
	
}
