package org.kishore.service;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class HelloServer {

	private final Server server;

	public HelloServer(int port) {
		this(ServerBuilder.forPort(port), port);
	}

	public HelloServer(ServerBuilder<?> serverBuilder, int port) {
		server = serverBuilder.addService(new HelloService()).build();
	}

	public void start() throws IOException {
		server.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may has been reset by its JVM shutdown hook.
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				HelloServer.this.stop();
				System.err.println("*** server shut down");
			}
		});

	}

	public void stop() {
		if (server != null) {
			server.shutdown();
		}
	}
	
	private void blockUntilShutdown() throws InterruptedException {
	    if (server != null) {
	      server.awaitTermination();
	    }
	  }

	public static void main(String... strings) throws Exception {
		HelloServer server = new HelloServer(8080);
		server.start();
		server.blockUntilShutdown();
	}
}
