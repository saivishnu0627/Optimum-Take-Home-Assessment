package com.example.demo.task2;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class WebSocketServer {

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("New connection: " + session.getId());
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("Received from " + session.getId() + ": " + message);
		session.getBasicRemote().sendText("Server echo: " + message);
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("Connection closed: " + session.getId());
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.err.println("Error on session " + session.getId() + ": " + throwable.getMessage());
	}
	
}