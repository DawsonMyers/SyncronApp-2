package ca.syncron.app.system;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import ca.syncron.app.Constants;
import ca.syncron.app.R;
import ca.syncron.app.network.Message;
import ca.syncron.app.network.UserBundle;
import ca.syncron.app.network.connection.Client;
import ca.syncron.app.network.connection.User;
import ca.syncron.app.system.ottoevents.EEventBus;
import ca.syncron.app.system.ottoevents.EventBus;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.sharedpreferences.Pref;
import roboguice.util.Ln;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EService
public class SyncronService extends Service {

	@App
	Syncron app;

	public static SyncronService me;
	//public volatile static int[]          digitalVals;
	//public        Syncron        app;
	private String           mUserName = "DawsonDroid";
	private String           mServerIp = "dawsonmyers.ca";
	private Message.UserType mUserType = Message.UserType.ANDROID;
	private static volatile int[] mDigital;
	private static volatile int[] mAnalog;
	static ArrayList<UpdateObserver>     analogObservers     = new ArrayList<>();
	static ArrayList<UpdateObserver>     digitalObservers    = new ArrayList<>();
	static ArrayList<UpdateObserver>     chatObservers       = new ArrayList<>();
	static ArrayList<ConnectionObserver> connectionObservers = new ArrayList<>();
	public ExecutorService               executor            = Executors.newCachedThreadPool();
	public ArrayList<UserBundle>         userBundles         = new ArrayList<>();


	public        ArrayList<User> users      = new ArrayList<>();
	public static boolean         mConnected = false;
	public  Client  mClient;
	private long    mSampleRate;
	private boolean mStreamEnabled;
	ArrayList<UserBundle> mUserBundles = new ArrayList<>();
	@Pref
	AppPrefs_ prefs;
	@Bean
	public
	EEventBus bus;
	private String mServerPort;
	private String mMessageTargetId;




	public String getStrUserBundles() {
		return strUserBundles;
	}

	public void setStrUserBundles(String strUserBundles) {
		this.strUserBundles = strUserBundles;
	}

	private String strUserBundles;

	public SyncronService() {
		//	executor.execute(() -> {
		// try {Thread.sleep(1500);} catch (InterruptedException e) {e.printStackTrace();}
		(mClient = new Client(this)).start();
		//	                 });

		me = this;
		app = Syncron.getInstance();
		app.setServiceRef(this);
		Toast.makeText(app, "Service Started", Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	public static SyncronService getInstance() {
		return me;
	}

	public String getUserName() {
		return mUserName;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		serviceNotification();
//		EventBus.register(this);
		bus.register(this);
		initPrefs();
//		mServerIp = prefs.serverIp().get();
//		mServerPort = prefs.serverPort().get();
//		Toast.makeText(Syncron.getInstance(), mServerIp, Toast.LENGTH_SHORT).show();
//
//		mUserName = prefs.userName().get();
	}
public void initPrefs() {
	try {
		mServerIp = prefs.serverIp().get();
		if (mServerIp.length()< 4) mServerIp = Constants.IpAddresses.IP;
		mServerPort = prefs.serverPort().getOr("6500");
		if (mServerPort.length()< 4) mServerPort = Constants.Ports.getTcp() + "";
		Toast.makeText(Syncron.getInstance(), mServerIp, Toast.LENGTH_SHORT).show();
		mUserName = prefs.userName().get();
		if (mUserName.length()< 2) mUserName = "Android";
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	public ArrayList<User> convertUser(ArrayList<UserBundle> list) {
		ArrayList<User> users = new ArrayList<>();
		for (UserBundle b : list) {
			users.add(new User(b));
		}
		return users;
	}

	public Message.UserType getUserType() {
		return mUserType;
	}

	public void updateStatus(Message msg) {
		userBundles = msg.getUserBundles();
		strUserBundles = userBundles.toString();
		if (userBundles != null) users = convertUser(userBundles);
		Log.e("updateStatus", strUserBundles);
	}

	public int[] getDigital() {
		return mDigital;
	}

	public int[] getAnalog() {
		return mAnalog;
	}

	public void setPin(String pin, boolean value) {

	}

	public static void observeAnalog(UpdateObserver observer, boolean register) {
		if (register) analogObservers.add(observer);
		else analogObservers.remove(observer);
	}

	public static void observeDigital(UpdateObserver observer, boolean register) {
		if (register) digitalObservers.add(observer);
		else digitalObservers.remove(observer);
	}

	public static void observeChat(UpdateObserver observer, boolean register) {
		if (register) chatObservers.add(observer);
		else chatObservers.remove(observer);
	}

	public static void observeConnection(ConnectionObserver observer, boolean register) {
		if (register) connectionObservers.add(observer);
		else connectionObservers.remove(observer);
	}

	public static void updateAnalog(int[] values) {
		setAnalog(values);
		for (UpdateObserver observer : analogObservers) {
			observer.updateAnalog(values);
		}
	}


	public static void setDigitalVals(int[] digitalVals) {
		SyncronService.mDigital = digitalVals;
	}

	public static void updateDigital(int[] values) {
		mDigital = values;
		for (UpdateObserver observer : digitalObservers) {
			observer.updateDigital(values);
		}
	}

	public static void setConnected(boolean connected) {
		mConnected = connected;
		for (ConnectionObserver observer : connectionObservers) {
			observer.connectionStatus(connected);
		}
	}

	public static void updateChat(String[] values) {
		for (UpdateObserver observer : analogObservers) {
			observer.updateChat(values);
		}
	}

	public void sendDigital(String pin, String value) {
		mClient.sendDigitalMessage(pin, value);
	}

	public void sendStream(long rate, boolean enabled) {
		setSampleRate(rate);
		setStreamEnabled(enabled);
		mClient.sendStreamMessage(null);
	}

	public void sendDigital(String pin, boolean value) {
		String val = value ? "1" : "0";
		sendDigital(pin, val);
	}

	public static void setAnalog(int[] analog) {
		mAnalog = analog;
	}

	public long getSampleRate() {
		return mSampleRate;
	}

	public boolean getStreamEnabled() {
		return mStreamEnabled;
	}

	public void setSampleRate(long sampleRate) {
		mSampleRate = sampleRate;
	}

	public void setStreamEnabled(boolean streamEnabled) {
		mStreamEnabled = streamEnabled;
	}

	public ArrayList<UserBundle> getUserBundles() {
		return mClient.getUserBundles();// mUserBundles;
	}

	@UiThread
	public void handleChat(Message msg) {
		bus.getInstance().newChatReceiveEvent(new User(msg), msg.getChatMessage());
	}

	public String getMessageTargetId() {
		return mMessageTargetId;
	}

	public void setMessageTargetId(String messageTargetId) {
		mMessageTargetId = messageTargetId;
	}

	public interface UpdateObserver {
		void updateAnalog(int[] values);
		void updateDigital(int[] values);
		void updateChat(String[] values);
	}

	public interface ConnectionObserver {
		void connectionStatus(boolean b);
	}

	public static boolean isConnected() {
		return mConnected;
	}

	public void serviceNotification() {
		// prepare intent which is triggered if the
// notification is selected

		Intent intent = new Intent(this, NotificationReceiver.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
		Notification n = new Notification.Builder(this)
				.setContentTitle("Data service running")
				.setContentText("Subject")
				.setSmallIcon(R.drawable.ic_service)
				.setContentIntent(pIntent)
				.setAutoCancel(true)
				.addAction(R.drawable.ic_action_connect, "Shutdown", pIntent).build();
//                              .addAction(R.drawable.syncron_filled, "More", pIntent)
//                              .addAction(R.drawable.syncron_filled, "And more", pIntent).build();*/


		NotificationManager notificationManager =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		notificationManager.notify(0, n);
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}


	private void tost(String s) {
		Toast.makeText(app, s , Toast.LENGTH_SHORT).show();
	}

	private void sendMessage(Message msg) {
		mClient.sendMessage(msg);
	}

	public String getServerIp() {
		return mServerIp;
	}

	public void setServerIp(String serverIp) {
		mServerIp = serverIp;
	}

	public String getServerPort() {
		return mServerPort;
	}

	public int getServerPortInt() {
		int port =Constants.Ports.getTcp() ; //6500;
		boolean ret = mServerPort==null;
		if (mServerPort==null) return port;
		int tmp = 0;

		try {
			tmp = Integer.parseInt(mServerPort);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return port;
		} finally {
		}
		return tmp > 1024 ? tmp :  port ;
	}

	public void setServerPort(String serverPort) {
		mServerPort = serverPort;
	}
	// Event Bus Callbacks
//////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Subscribe
	public void onSendChat(EventBus.ChatSendEvent event) {
		Ln.d("SEND CHAT EVENT");
		Toast.makeText(Syncron.getInstance(), "Chat: " + event.mMsg, Toast.LENGTH_SHORT).show();
		mClient.sendChatMessage(Message.newChat(event.mMsg));
	}

	@Subscribe
	public void onSendChat(EventBus.ChatReceiveEvent event) {
		Ln.d("RECEIVE CHAT EVENT");
		Toast.makeText(Syncron.getInstance(), "Chat: " + event.mMsg, Toast.LENGTH_SHORT).show();
		//	mClient.sendChatMessage( Message.newChat(event.mMsg));
	}
	@Subscribe
	public void onSendTarget(EventBus.SendTargetEvent e) {
		e.msg.setTargetId(e.userId);
		sendMessage(e.msg);
	}

	@Subscribe
	public void onSetTargetId(EventBus.SetTargetIdEvent e) {
		setMessageTargetId(e.targetId);
		tost("Target Id: " + getMessageTargetId());
	}
}
