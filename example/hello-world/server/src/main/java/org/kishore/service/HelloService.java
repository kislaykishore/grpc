package org.kishore.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kishore.HelloGrpc;
import org.kishore.HelloRequest;
import org.kishore.HelloResponse;

import io.grpc.stub.StreamObserver;

public class HelloService extends HelloGrpc.HelloImplBase {

	@Override
	public void sayHello(HelloRequest request,
	        StreamObserver<org.kishore.HelloResponse> responseObserver) {
		String greeting = request.getGreeting();
		HelloResponse response = HelloResponse.newBuilder().setReply(greeting + " to you too!").build();
		responseObserver.onNext(response);
		//responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	@Override
	public void sayHelloFromServer(HelloRequest request,
	        StreamObserver<HelloResponse> responseObserver) {
		String greeting = request.getGreeting();
		for(String greet : Arrays.asList("Goodies", "Howdy", "Salaam Ale Kum")) {
			HelloResponse response = HelloResponse.newBuilder().setReply(greet + " from server!").build();
			responseObserver.onNext(response);
		}
		HelloResponse lastResponse = HelloResponse.newBuilder().setReply("Final Greetings: " + greeting).build();
		responseObserver.onNext(lastResponse);
		responseObserver.onCompleted();
	}

	@Override
	 public StreamObserver<HelloRequest> sayHelloStreamed(
		        final StreamObserver<HelloResponse> responseObserver) {
		
		return new StreamObserver<HelloRequest>() {
			private final List<String> greetings = new ArrayList<>();
			
			@Override
			public void onNext(HelloRequest value) {
				String greeting = value.getGreeting();
				greetings.add(greeting);
				for(String greet : greetings) {
					HelloResponse response = HelloResponse.newBuilder().setReply(greet).build();
					responseObserver.onNext(response);
				}
				
			}
			
			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}
		};
	}
}
