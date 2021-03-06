package ca.syncron.app.network;

import ca.syncron.app.MessageProcessor;
import ca.syncron.app.system.SyncronService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Dawson on 3/15/2015.
 */

/**
 * @author Dawson
 */
// Configure Jackson to only encode fields that are non-null
@JsonInclude(JsonInclude.Include.NON_NULL)

// Configure Jackson to exlcude the user and serialized message string
@JsonIgnoreProperties({"mTargetUser", "mSerialMessage"})
@SuppressWarnings("ALL")
public class Message {
	static String nameId = Message.class.getSimpleName();

	@JsonIgnore
	public final static Logger log = Logger.getAnonymousLogger();
	@JsonIgnore
	public  MessageProcessor mapper;
	private Date             mTimestamp;
	private boolean          mIsTargeted;

	public void setSerialMessage(String serialMessage) {
		mSerialMessage = serialMessage;
	}

	public String getSerialMessage() {
		return mSerialMessage;
	}

	public static Message newChat(String strMsg) {
		Message msg = new Message();
		msg.init();
		msg.setChatMessage(strMsg);
		msg.setMessageType(MessageType.CHAT);
		msg.setChatType(Chat.USERS);
		return msg;
	}

	private void init() {
		setUserName(SyncronService.getInstance().getUserName());
//		setMessageType(MessageType.CHAT);
//		setChatType(Chat.USERS);

	}


	public enum MessageType {DIGITAL, ANALOG, ADMIN, UPDATE, REGISTER, LOGIN, STATUS, CHECKIN, USER, STREAM, CHAT, QUERY, ERROR, UNKNOWN, ACCESS, TARGET, SUBSCRIBE;}

	public enum UserType {NODE, SERVER, ANDROID, NODE_SERVER, NODE_CLIENT, UNKNOWN;}

	public enum Chat {REGISTER, UPDATE, LOGIN, USERS, DISCONNECT, UNKNOWN;}

	@JsonIgnore
	//User mUser;
			String mSerialMessage;
	//User   mTargetUser;

	@JsonCreator
	public Message() {}

	public Message(MessageType msgType, UserType senderType) {
		this();
		mMessageType = msgType;
		mUserType = senderType;
	}

	public Message(UserType sender, UserType target) {
		this();
		mUserType = sender;
		mTargetType = target;
	}
//	}

/*	@JsonIgnore
	//public User getUser() {
		return mUser;
	}

	@JsonIgnore
	public void setUser(User user) {
		mUser = user;
	}


	@JsonIgnore
	public String getSerialMessage() {
		return mSerialMessage;
	}

	@JsonIgnore
	public void setSerialMessage(String serialMessage) {
		mSerialMessage = serialMessage;
	}

	@JsonIgnore
	public User getTargetUser() {
		return mTargetUser;
	}

	@JsonIgnore
	public void setTargetUser(User targetUser) {
		this.mTargetUser = targetUser;
	}*/

	//
//	public String serialize() {
//		setSerialMessage(mapper.serializeMessage(this));
//		return getSerialMessage();
//	}
//
//	public Message deserialize(String serialMessage) {
//		return mapper.deserializeMessage(serialMessage);
//	}
	@JsonIgnore
	public synchronized String serializeMessage(/*Message msg*/) {
		if (this == null) {
			log.info("Message :Serialize - Null message");
			return null;
		}
		StringWriter writer = new StringWriter();
		ObjectMapper mapper = new ObjectMapper();
		//setFormat(true);
		//mapper = new ObjectMapper();
		//mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		String s = "";
		try {
			//	mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			mapper.writeValue(writer, this);
			s = writer.toString();
			log.info("serializeMessage");
//			setSerialMessage(s);
			mSerialMessage = s;
			//log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(msg));
			log.info("\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
		} catch (IOException e) {
			e.printStackTrace();

		}
		return s;
	}

	@JsonIgnore
	public synchronized Message deserializeMessage(String msgString) {
		Message msg = new Message();
		try {

			StringWriter writer = new StringWriter();
			ObjectMapper mapper = new ObjectMapper();
			//setFormat(true);
			log.info("deserializeMessage");
			msg = mapper.readValue(msgString, Message.class);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return msg;
	}

	@JsonIgnore
	public void digital(String pin, String value) {
		setMessageType(MessageType.DIGITAL);
		setPin(pin);
		setValue(value);
	}

	//  reqResponse used to request a list of connected users
	@JsonIgnore
	public void chat(String chatMessage, boolean reqResponse) {
		setMessageType(MessageType.CHAT);
		setChatMessage(chatMessage);
		setReqResponse(reqResponse);
	}

	@JsonIgnore
	public void login(String name, String password) {
		setMessageType(MessageType.LOGIN);
		setLogin(name + "_" + password);
	}

	@JsonIgnore
	public void query(String query, Map<String, Object> queryResult) {
		setMessageType(MessageType.QUERY);
		setQuery(query);
		setQueryResult(queryResult);
	}

	@JsonIgnore
	public void register(String userId, boolean isAdmin, boolean reqResponse) {
		setMessageType(MessageType.REGISTER);
		setUserId(userId);
		setAdmin(isAdmin);
		setReqResponse(reqResponse);
	}

	@JsonIgnore
	public void register(boolean reqResponse) {
		setMessageType(MessageType.REGISTER);
		setReqResponse(reqResponse);
	}

	@JsonIgnore
	public void update(int[] digitalValues, int[] analogValues) {
		setMessageType(MessageType.UPDATE);
		setDigitalValues(digitalValues);
		setAnalogValues(analogValues);
	}

	@JsonIgnore
	public void admin(String adminCommad) {
		setMessageType(MessageType.ADMIN);
		setAdminCommad(adminCommad);
	}
	public Message newUpdate(SyncronService c) {
		Message msg = new Message(MessageType.UPDATE, UserType.ANDROID);
		msg.setDigitalValues(c.getDigital());
		msg.setAnalogValues(c.getAnalog());
		return msg;
	}
	@JsonIgnore
	public void checkIn(Message m) {
		setMessageType(MessageType.CHECKIN);
	}

	@JsonIgnore
	public void status(Message m) {setMessageType(MessageType.STATUS);}

	//  Message fields
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	MessageType           mMessageType;
	String                mUserId;
	UserType              mUserType;
	UserType              mSenderType;
	String                mSenderId;
	UserType              mTargetType;
	String                mTargetId;
	String                mChatMessage;
	String                mLogin;
	String                mAdminId;
	Chat                  mChatType;
	String                mPin;
	String                mValue;
	String                mTestString;
	String                mUserName;
	ArrayList<UserBundle> mUserBundles;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	String mName;

	String[] tags;

	String              mQuery;
	Map<String, Object> mQueryResult;

	boolean mAdmin;
	boolean mReqResponse;
	boolean mDoSubsribe;

	int[] mAnalogValues;
	int[] mDigitalValues;

	String mAdminCommad;

	String  mNodeServerId;
	boolean isTargeted;

	long sampleRate;

	boolean streamEnabled;
	public boolean getIsTargeted() {
		return mIsTargeted;
	}
	public boolean setIsTargeted() {
		return mIsTargeted;
	}
	public void setDoSubsribe(boolean doSubsribe) {
		mDoSubsribe = doSubsribe;
	}

	public boolean getDoSubsribe() {
		return mDoSubsribe;
	}
	public void setTimestamp(Date timestamp) {
		mTimestamp = timestamp;
	}

	public Date getTimestamp() {
		return mTimestamp;
	}
	//////////////////////////////////////////////////////////////////
	public void setUserBundles(ArrayList<UserBundle> userBundles) {
		mUserBundles = userBundles;
	}

	public boolean getStreamEnabled() {
		return streamEnabled;
	}

	public long getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(long sampleRate) {
		this.sampleRate = sampleRate;
	}

	public boolean isStreamEnabled() {
		return streamEnabled;
	}

	public void setStreamEnabled(boolean streamEnabled) {
		this.streamEnabled = streamEnabled;
	}

	public ArrayList<UserBundle> getUserBundles() {
		return mUserBundles;
	}

	/**
	 * @return object messageType of type MessageType
	 */
	public MessageType getMessageType() {
		return this.mMessageType;
	}

	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(MessageType messageType) {
		this.mMessageType = messageType;
	}

	/**
	 * @return object pin of type String
	 */
	public String getPin() {
		return this.mPin;
	}

	@JsonIgnore
	public int getPinAsInt() {
		return Integer.parseInt(this.mPin);
	}

	/**
	 * @param pin the pin to set
	 */
	public void setPin(String pin) {
		this.mPin = pin;
	}

	/**
	 * @return object value of type String
	 */
	public String getValue() {
		return this.mValue;
	}

	@JsonIgnore
	public int getValueAsInt() {
		return Integer.parseInt(this.mValue);
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.mValue = value;
	}

	/**
	 * @return object userId of type String
	 */
	public String getUserId() {
		return this.mUserId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.mUserId = userId;
	}

	/**
	 * @return object userType of type UserType
	 */
	public UserType getUserType() {
		return this.mUserType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(UserType userType) {
		this.mUserType = userType;
	}

	/**
	 * @return object senderType of type string
	 */
	public UserType getSenderType() {
		return this.mSenderType;
	}

	/**
	 * @param senderType the senderType to set
	 */
	public void setSenderType(UserType senderType) {
		this.mSenderType = senderType;
	}

	/**
	 * @return object senderId of type String
	 */
	public String getSenderId() {
		return this.mSenderId;
	}

	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.mSenderId = senderId;
	}

	/**
	 * @return object targetId of type String
	 */
	public String getTargetId() {
		return this.mTargetId;
	}

	/**
	 * @param targetId the targetId to set
	 */
	public void setTargetId(String targetId) {
		this.mTargetId = targetId;
	}

	/**
	 * @return object targetType of type String
	 */
	public UserType getTargetType() {
		return this.mTargetType;
	}

	/**
	 * @param targetType the targetType to set
	 */
	public void setTargetType(UserType targetType) {
		this.mTargetType = targetType;
	}

	/**
	 * @return object chatMessage of type String
	 */
	public String getChatMessage() {
		return this.mChatMessage;
	}

	/**
	 * @param chatMessage the chatMessage to set
	 */
	public void setChatMessage(String chatMessage) {
		this.mChatMessage = chatMessage;
	}

	/**
	 * @return object testString of type String
	 */
	public String getTestString() {
		return this.mTestString;
	}

	/**
	 * @param testString the testString to set
	 */
	public void setTestString(String testString) {
		this.mTestString = testString;
	}

	/**
	 * @return object login of type String
	 */
	public String getLogin() {
		return this.mLogin;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.mLogin = login;
	}

	/**
	 * @return object adminId of type String
	 */
	public String getAdminId() {
		return this.mAdminId;
	}

	/**
	 * @param adminId the adminId to set
	 */
	public void setAdminId(String adminId) {
		this.mAdminId = adminId;
	}

	/**
	 * @return object chatType of type Chat
	 */
	public Chat getChatType() {
		return this.mChatType;
	}

	/**
	 * @param chatType the chatType to set
	 */
	public void setChatType(Chat chatType) {
		this.mChatType = chatType;
	}

	/**
	 * @return object query of type String
	 */
	public String getQuery() {
		return this.mQuery;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.mQuery = query;
	}

	/**
	 * @return object queryResult of type Map<String,Object>
	 */
	public Map<String, Object> getQueryResult() {
		return this.mQueryResult;
	}

	/**
	 * @param queryResult the queryResult to set
	 */
	public void setQueryResult(Map<String, Object> queryResult) {
		this.mQueryResult = queryResult;
	}

	/**
	 * @return object admin of type boolean
	 */
	public boolean isAdmin() {
		return this.mAdmin;
	}

	/**
	 * @param admin the admin to set
	 */
	public void setAdmin(boolean admin) {
		this.mAdmin = admin;
	}

	/**
	 * @return object reqResponse of type boolean
	 */
	public boolean isReqResponse() {
		return this.mReqResponse;
	}

	/**
	 * @param reqResponse the reqResponse to set
	 */
	public void setReqResponse(boolean reqResponse) {
		this.mReqResponse = reqResponse;
	}

	/**
	 * @return object analogValues of type int[]
	 */
	public int[] getAnalogValues() {
		return this.mAnalogValues;
	}

	/**
	 * @param analogValues the analogValues to set
	 */
	public void setAnalogValues(int[] analogValues) {
		this.mAnalogValues = analogValues;
	}

	/**
	 * @return object digitalValues of type boolean[]
	 */
	public int[] getDigitalValues() {
		return this.mDigitalValues;
	}

	/**
	 * @param digitalValues the digitalValues to set
	 */
	public void setDigitalValues(int[] digitalValues) {
		this.mDigitalValues = digitalValues;
	}

	/**
	 * @return object adminCommad of type String
	 */
	public String getAdminCommad() {
		return this.mAdminCommad;
	}

	/**
	 * @param adminCommad the adminCommad to set
	 */
	public void setAdminCommad(String adminCommad) {
		this.mAdminCommad = adminCommad;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String userName) {
		mUserName = userName;
	}

}