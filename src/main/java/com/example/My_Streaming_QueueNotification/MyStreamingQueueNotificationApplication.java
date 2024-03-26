package com.example.My_Streaming_QueueNotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.azure.messaging.servicebus.*;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MyStreamingQueueNotificationApplication {
	private static final String connectionString = "Endpoint=sb://my-streaming.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=UCS0sOHcAcyB9FtP62mpuAvA2/NlJtiBt+ASbJC83+s=";
	private static final String queueName = "notify_queue";

	public static void main(String[] args) throws InterruptedException {
		ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
				.connectionString(connectionString)
				.processor()
				.queueName(queueName)
				.processMessage(MyStreamingQueueNotificationApplication::processMessage)
				.processError(context -> processError(context))
				.buildProcessorClient();

		System.out.println("Starting the processor");
		processorClient.start();

		TimeUnit.SECONDS.sleep(10);
		System.out.println("Stopping and closing the processor");
		processorClient.close();
	}

	private static void processMessage(ServiceBusReceivedMessageContext context) {
		String receive_message = context.getMessage().getBody().toString();
		System.out.println("Received message: " + receive_message);

		context.complete();
	}
	private static void processError(ServiceBusErrorContext context) {
		System.err.println("Error occurred: " + context.getException().getMessage());
		context.getException().printStackTrace();
	}

}
