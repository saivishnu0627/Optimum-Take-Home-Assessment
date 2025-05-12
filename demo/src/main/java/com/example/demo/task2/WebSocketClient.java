package com.example.demo.task2;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebSocketClient {

	private Session session;

	public WebSocketClient(URI endpointURI) {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
		System.out.println("Connected to server");
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println("Received from server: " + message);
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		System.out.println("Disconnected from server: " + reason);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.err.println("Error: " + throwable.getMessage());
	}

	public void sendMessage(String message) throws IOException {
		if (session != null && session.isOpen()) {
			session.getBasicRemote().sendText(message);
		} else {
			System.err.println("Cannot send message. No open session.");
		}
	}

	public static void main(String[] args) throws Exception {
		WebSocketClient client = new WebSocketClient(new URI("ws://localhost:8080/chat"));
		client.sendMessage("Hello Server!");
		Thread.sleep(5000); // Keep connection open for 5 seconds
		client.sendMessage("Goodbye Server!");
		Thread.sleep(1000); // Allow some time for message exchange
	}
	
}