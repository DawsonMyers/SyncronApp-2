package ca.syncron.app.network.connection;

import ca.syncron.app.Constants;
import ca.syncron.app.network.Message;
import ca.syncron.app.network.UserBundle;
import ca.syncron.app.system.SyncronService;
import ca.syncron.app.system.ottoevents.EventBus;
import naga.NIOServerSocket;
import naga.NIOSocket;
import naga.SocketObserver;
import naga.eventmachine.EventMachine;
import naga.packetreader.AsciiLinePacketReader;
import naga.packetwriter.AsciiLinePacketWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import static java.lang.System.out;


/**
 * Created by Dawson on 3/7/2015.
 */
public class Client extends Thread implements SocketObserver, Handler.MessageCallbacks.DispatchCallbacks {
	static              String nameId = Client.class.getSimpleName();
	public final static Logger log    = LoggerFactory.getLogger(nameId);
	public static SyncronService mController;
	public static String         userName;
	public static Handler        mHandler;
	//public static volatile Map<String, User>    mUserMap    = new HashMap<String, User>();    // new
	private static Client me = null;
	//private static AbstractController   mController = AbstractController.getInstance();
	public EventMachine mEventMachine;
	NIOServerSocket mServerSocket;
	//public List<User> mUsers;
	private boolean mIsServer = false;
	public NIOSocket mSocket;
	public boolean   mConnected;
//@Inject
	EventBus bus = EventBus.getInstance();
	public ArrayList<UserBundle> getUserBundles() {
		return mUserBundles;
	}

	public void setUserBundles(ArrayList<UserBundle> userBundles) {
		mUserBundles = userBundles;
	}

	public ArrayList<UserBundle> mUserBundles = new ArrayList<>();

	public Client() {
		me = this;

		mHandler = new Handler(this);
		mHandler.start();
	}

	public Client(SyncronService controller) {
		this();
		//AppRegistrar.register(this);
		mController = controller;
		//isServer(false);
		//registerCallbacks(callbacks);
	}


	public boolean isReconnecting() {
		return reconnecting;
	}

	public void setReconnecting(boolean reconnecting) {
		this.reconnecting = reconnecting;
	}

	private boolean reconnecting;

	//  Client Callbacks
	// ///////////////////////////////////////////////////////////////////////////////////
	@Override
	public void connectionOpened(NIOSocket nioSocket) {
		mSocket = nioSocket;
		isConnected(true);
		setReconnecting(false);
	}

	private void isConnected(boolean b) {
		mConnected = b;
		SyncronService.setConnected(b);
	}

	private boolean isConnected() {
		return mConnected;
	}

	@Override
	public void connectionBroken(NIOSocket nioSocket, Exception exception) {
		log.error("Disconnected");
		isConnected(false);
		if (isReconnecting()) return;
		reconnect();

	}

	@Override
	public void packetReceived(NIOSocket socket, byte[] packet) {
		out.println("");
		log.info("Packet received");
		mHandler.addToReceiveQueue(new String(packet));

	}

	@Override
	public void packetSent(NIOSocket socket, Object tag) {
		log.info("Packet sent");
	}

	public void run() {
		connect();
	}

	public void connect() {
		int port = Constants.Ports.getTcp();
		//  Client
		String ip = mController.getServerIp();// Constants.IpAddresses.IP;
		try {
			log.info("Starting Client");
			EventMachine machine = new EventMachine();

			InetAddress ipAddress = InetAddress.getByName(ip);
			mSocket = machine.getNIOService().openSocket(ipAddress, port);
			mSocket.listen(me);
			mSocket.setPacketReader(new AsciiLinePacketReader());
			mSocket.setPacketWriter(new AsciiLinePacketWriter());
			machine.start();
			if (mSocket.isOpen()) mConnected = true;
			//if(isScheduled && mSocket.isOpen()) scheduler.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public synchronized void reconnect() {
		reconnecting = true;
		int count = 0;
		int index = 0;
		int port = Constants.Ports.getTcp();
		//  Client
		String ip = Constants.IpAddresses.IP;
		try {
			EventMachine machine = mEventMachine;
			InetAddress ipAddress = InetAddress.getByName(ip);
			log.error("Attempting to reconnect to server");
			while (!isConnected()) {
				count++;
				if (mSocket.isOpen()) break;
				try {
					connect();
					index++;
					connect();
					if (index >= 3) {
						log.error("Connection attempts: " + count);
						index = 0;
					}
					Thread.sleep(5000);

					if (mSocket.isOpen()) mConnected = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void handleDigitalMessage(Message msg) {

		log.info("handleDigitalMessage");
		log.error("setPin(" + msg.getPin() + ", " + msg.getValue() + ")");
		mController.updateDigital(msg.getDigitalValues());
		//mController.setPin(msg.getPinAsInt(), msg.getValueAsInt());
	}

	@Override
	public void handleAnalogMessage(Message msg) {

	}

	@Override
	public void handleChatMessage(Message msg) {

mController.handleChat(msg); //bus.getInstance().newChatReceiveEvent(new User(msg),msg.getChatMessage());
	}

	@Override
	public void handleStreamMessage(Message msg) {

	}

	@Override
	public void handleAdminMessage(Message msg) {

	}

	@Override
	public void handleUpdateMessage(Message msg) {
		SyncronService.updateAnalog(msg.getAnalogValues());
		SyncronService.updateDigital(msg.getDigitalValues());
	}

	@Override
	public void handleRegisterMessage(Message msg) {

		msg.register(false);
		msg.setUserName(mController.getUserName());
		msg.setUserType(mController.getUserType());
		mHandler.addToSendQueue(msg);
	}

	@Override
	public void handleStatusMessage(Message msg) {
setUserBundles(msg.getUserBundles());
		mController.updateStatus(msg);
	}

	@Override
	public void handleLoginMessage(Message msg) {
	}

	@Override
	public void handleUserMessage(Message msg) {

	}

	@Override
	public void handleErrorMessage(Message msg) {

	}

	//  Sending
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void sendMessage(Message msg) {


		if (mSocket != null) {
			if (msg.getSerialMessage() == null) {
				msg.serializeMessage();
				log.debug("sendMessage:: serializing msg to send");
				//return;
			}
			mSocket.write(msg.getSerialMessage().getBytes());
			log.info("Message sent");
		}
	}

	@Override
	public void sendUpdateMessage(Message msg) {
		if (msg == null) {
			msg = new Message(Message.UserType.ANDROID, Message.UserType.SERVER);
			msg.update(mController.getDigital(), mController.getAnalog());
		}
		sendMessage(msg);
	}
	public   void sendStream(long rate, boolean enabled) {

	}
	public void sendStreamMessage(Message msg) {
		if (msg != null) {
			sendMessage(msg);
			return;
		}
		msg = new Message();
		msg.setSampleRate(mController.getSampleRate());
		msg.setStreamEnabled(mController.getStreamEnabled());
		sendMessage(msg);
	}
	@Override
	public void sendChatMessage(Message msg) {
sendMessage(msg);
	}

	@Override
	public void sendSystemMessage(Message msg) {

	}

	@Override
	public void sendRegisterMessage(Message msg) {

	}

	@Override
	public void sendDigitalMessage(Message msg) {

	}

	@Override
	public void sendDigitalMessage(int pin, int value) {
		sendDigitalMessage(pin + "", value + "");
	}

	@Override
	public void sendDigitalMessage(String pin, String value) {
		Message msg = new Message(Message.MessageType.DIGITAL, Message.UserType.ANDROID);
		msg.setPin(pin);
		msg.setValue(value);
		sendMessage(msg);
	}

	@Override
	public void sendAnalogMessage(Message msg) {

	}

	@Override
	public <T> void processMessage(T msg) {

	}

	public static String getNameId() {return nameId;}

	public void registerCallbacks(Handler.MessageCallbacks.DispatchCallbacks callbacks) {mHandler.register(callbacks);}
}
