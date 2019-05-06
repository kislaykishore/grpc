/**
 * 
 */
package org.kishore.client;

import org.kishore.HelloGrpc;
import org.kishore.HelloGrpc.HelloBlockingStub;
import org.kishore.HelloGrpc.HelloStub;
import org.kishore.HelloRequest;
import org.kishore.HelloResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * @author kislay
 *
 */
public class HelloClient {

	private final ManagedChannel channel;

	private final HelloBlockingStub blockingStub;

	private final HelloStub asyncStub;

	public HelloClient(String host, int port) {
		this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
	}

	public HelloClient(ManagedChannelBuilder<?> channelBuilder) {
		channel = channelBuilder.build();
		blockingStub = HelloGrpc.newBlockingStub(channel);
		asyncStub = HelloGrpc.newStub(channel);

	}

	public static void main(String... strings) throws Exception {
		HelloClient client = new HelloClient("localhost", 8080);
		/*
		 * HelloRequest request =
		 * HelloRequest.newBuilder().setGreeting("Good Day").build(); HelloResponse
		 * response = client.blockingStub.sayHello(request); /*
		 * StreamObserver<HelloResponse> observer = new StreamObserver<HelloResponse>()
		 * {
		 * 
		 * @Override public void onNext(HelloResponse value) {
		 * System.out.println("Async output: " + value.getReply()); }
		 * 
		 * @Override public void onError(Throwable t) { // TODO Auto-generated method
		 * stub
		 * 
		 * }
		 * 
		 * @Override public void onCompleted() { System.out.println("Completed");
		 * 
		 * } }; client.asyncStub.sayHello(request, observer);
		 * System.out.println("Received: " + response.getReply()); Thread.sleep(2000);
		 

		List<String> greetings = Arrays.asList("Hello", "Howdy", "Good Day", "Good Morning");

		StreamObserver<HelloRequest> observer = client.asyncStub.sayHelloStreamed(new StreamObserver<HelloResponse>() {

			@Override
			public void onNext(HelloResponse value) {
				System.out.println(value.getReply());
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub

			}

		});
		for(String greet : greetings) {
			HelloRequest request = HelloRequest.newBuilder().setGreeting(greet).build();
			observer.onNext(request);
			
		}
		*/
		HelloRequest request = HelloRequest.newBuilder().setGreeting("What a wonderful day!").build();
		client.asyncStub.sayHelloFromServer(request, new StreamObserver<HelloResponse>() {
			@Override
			public void onNext(HelloResponse value) {
				System.out.println(value.getReply());
			}
			
			@Override
			public void onError(Throwable t) {
				synchronized (request) {
					request.notify();
				}
			}
			
			@Override
			public void onCompleted() {
				synchronized (request) {
					request.notify();
				}
			}
			
		});
		synchronized(request) {
            request.wait(10000);
		}

        System.out.println("Completed");
	}
}
