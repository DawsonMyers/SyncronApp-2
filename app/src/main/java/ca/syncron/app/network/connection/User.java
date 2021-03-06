package ca.syncron.app.network.connection;

import ca.syncron.app.network.Message;
import ca.syncron.app.network.UserBundle;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Dawson on 3/22/2015.
 */
public class User {
	static              String           nameId = User.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
	//String nameId = UserBundle.class.getSimpleName();
	//public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
	public final String name;//   = "NotSet";
	public final String type;//   = Message.UserType.ANDROID;

	public final String userId;//= "NotSet";
	public final String timeStamp;// = new Date();
	public final String accessLevel;//= ca.syncron.app.Constants.Access.USER;

	public String getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getType() {
		return type;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public User(UserBundle bundle) {
		name = bundle.getName();
		type = bundle.getType().toString();
		userId = bundle.getUserId();

		if (bundle.getTimeStamp()  != null) timeStamp = bundle.getTimeStamp().toString();
		else timeStamp = "null";
		accessLevel = bundle.getAccessLevel().toString();
		log.error("USER ITEM BOUND");
	}
	public User(Message msg) {
		name = msg.getName();
		type = String.valueOf(msg.getUserType());
		userId = msg.getUserId();
		timeStamp =new Date().toString();
		accessLevel = "User";
	}
//	public User(UserBundle bundle) {
//		name = bundle.getName();
//		type = bundle.getType().toString();
//		userId = bundle.getUserId();
//		timeStamp = bundle.getTimeStamp().toString();
//		accessLevel = bundle.getAccessLevel().toString();
//		log.error("USER ITEM BOUND");
//	}
}
