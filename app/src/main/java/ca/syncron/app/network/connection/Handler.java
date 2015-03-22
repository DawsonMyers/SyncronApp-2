package ca.syncron.app.network.connection;

import ca.syncron.app.MessageProcessor;
import ca.syncron.app.network.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ca.syncron.app.network.connection.Handler.MessageCallbacks.*;

/**
 * Created by Dawson on 3/7/2015.
 */
public class Handler extends Thread {
	public final static Logger        log            = LoggerFactory.getLogger(Handler.class.getName());
	public volatile     MessageLooper mMessageLooper = new MessageLooper( );
	ExecutorService executor = Executors.newSingleThreadExecutor();
	static public MessageProcessor mapper;
	public static Client           mConnector;
	public        int              receiveCounter;
	public        int              sendCounter;

	public Handler() {}

//	public Handler(DispatchCallbacks callbacks) {
//		mapper = new MessageProcessor();
//		mMessageLooper = new MessageLooper(callbacks);
//		mMessageLooper.register(callbacks);
//	}

//	Message msg = MessageBuilder
//			.newMessage().withAdmin(true).withChatMessage("chat")
//			.withMessageType(Message.MessageType.CHAT)
//			.build();


	public Handler(Client connector) {
		mConnector = connector;
		mMessageLooper = new MessageLooper(connector);
		mMessageLooper.register(connector);
//		receiveCounter = connector.getReceiveCounter();
//		sendCounter = connector.getSendCounter();
		  mapper = new MessageProcessor();
	  }



	  public synchronized void addToReceiveQueue(String msgString) {
		  //	executor.execute(() -> {
		  Message msg = new Message();

		  if (!msgString.trim().startsWith("{") && !msgString.trim().endsWith("}")) {
			  log.info("addToReceiveQueue - message did not start/end with braces");
			  //log.info(msg);
			  return;
		  }
		  //log.info(mapper.smsgString);
		  msg = mapper.deserializeMessage(msgString);
		  //msg = executor.submit(() -> mapper.deserializeMessage(msgString)).get();
		  if (msg != null & msg.getMessageType() != null) {
			  mMessageLooper.addToReceiveQue(msg);

			  log.info("Message added to ReceiveQueue");
		  } else {
			  log.error("addtoReceiveQueue", "Null deserialization returned a null message");
			  msg.setMessageType(Message.MessageType.ERROR);
			  log.info("addtoReceiveQueue - Null deserialization returned a null message");
		  }
		  //});
	  }

	  public void addToReceiveQueue(Message msg) {
		  receiveCounter++;
		  mMessageLooper.addToReceiveQue(msg);
	  }

	  public void addToSendQueue(Message msg) {
		  sendCounter++;

		  mMessageLooper.addToSendQue(msg);
	  }


	public void register(DispatchCallbacks callbacks) {
		mMessageLooper.register(callbacks);
	}

	//
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public interface MessageCallbacks {
		public abstract void handleDigitalMessage(Message msg);

		public abstract void handleAnalogMessage(Message msg);

		public abstract void handleChatMessage(Message msg);

		public	interface CompatCallbacks<T> {
			Message.MessageType getSysMessageType();

			String getSysMessage();

			String getSysUserId();
		}

		public	interface QueueCallbacks {
			void handleMessage();

			void processMessage(Message msg);

		}


		public interface DispatchCallbacks {
			public abstract void handleDigitalMessage(Message msg);

			public abstract void handleAnalogMessage(Message msg);

			public abstract void handleChatMessage(Message msg);
			public abstract void handleStreamMessage(Message msg);
			public abstract void handleAdminMessage(Message msg);

			public abstract void handleUpdateMessage(Message msg);

			public abstract void handleRegisterMessage(Message msg);

			public abstract void handleStatusMessage(Message msg);

			public abstract void handleLoginMessage(Message msg);

			public abstract void handleUserMessage(Message msg);
			public abstract void handleErrorMessage(Message msg);

			public abstract void sendMessage(Message msg);
			public abstract void sendUpdateMessage(Message msg);
			public abstract void sendChatMessage(Message msg);
			public abstract void sendSystemMessage(Message msg);
			public abstract void sendRegisterMessage(Message msg);
			public abstract void sendDigitalMessage(Message msg);
			public abstract void sendDigitalMessage(int pin, int value);
			public abstract void sendDigitalMessage(String  pin, String  value);
			public abstract void sendAnalogMessage(Message msg);
			<T> void processMessage(T msg);
		}


	}
}