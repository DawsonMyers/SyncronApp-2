package ca.syncron.app;


import android.util.Log;
import ca.syncron.app.network.Message;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.util.Log.d;

/**
 * Created by Dawson on 3/15/2015.
 */
public class MessageProcessor {
	static String nameId = MessageProcessor.class.getSimpleName();
	//public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
	ExecutorService executor = Executors.newSingleThreadExecutor();
	//public        MessageProcessor mProcessor = new MessageProcessor();
	//public final static Logger log = LoggerFactory.getLogger(MessageProcessor.class.getName());
	//Message      msg;
	StringWriter writer;
	ObjectMapper mapper;

	public MessageProcessor() {
		writer = new StringWriter();
		mapper = new ObjectMapper();
		setFormat(false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
	}

	public static MessageProcessor newProcessor() {
		return new MessageProcessor();
	}

	public void setFormat(boolean bool) {
		mapper.configure(SerializationFeature.INDENT_OUTPUT, bool);
	}

	public synchronized String serializeMessage(Object msg) {
		if (msg == null) {
			Log.d(nameId, "MessageProcessor:Serialize - Null message");
			return null;
		}
		StringWriter	writer = new StringWriter();
		ObjectMapper	mapper = new ObjectMapper();
		//setFormat(true);
		//mapper = new ObjectMapper();
		//mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		try {
			mapper.writeValue(writer, msg);
			//log.info("serializeMessage");
			Log.d(nameId, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg));
			//log.info("\n\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg));
		} catch (IOException e) {
			e.printStackTrace();

		}
		return writer.toString();
	}

	public synchronized ca.syncron.app.network.Message deserializeMessage(String msgString) {
		Message msg = new Message();
		try {
			StringWriter	writer = new StringWriter();
			ObjectMapper	mapper = new ObjectMapper();
			//setFormat(true);
			//out.println(mapper.writeValueAsString(msgString));


			d("processor","deserializeMessage");
			msg = mapper.readValue(msgString, Message.class);

			mapper.writeValue(writer, msg);
			Log.d(nameId, "\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg));
		} catch (IOException e) {
			e.printStackTrace();

		}
		return msg;
	}
}

