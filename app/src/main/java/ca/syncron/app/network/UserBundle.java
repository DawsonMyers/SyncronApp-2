package ca.syncron.app.network;

import ca.syncron.app.Constants;
import ca.syncron.app.network.connection.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.Date;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Created by Dawson on 3/12/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBundle {
	private String           nameId = UserBundle.class.getSimpleName();
	//public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
	private String           name   = "NotSet";
	private Message.UserType type   = Message.UserType.ANDROID;
	private String           userId = "NotSet";
	private Date timeStamp;// = new Date();
	private Constants.Access accessLevel = Constants.Access.USER;
//	public HashMap<String, NodeClientBundler.NodeBundle> nodes = new HashMap<>();

	@JsonCreator
	public UserBundle() { }

//	public UserBundle(User user) {
//		name = user.getName();
//		type = user.getType();
//		userId = user.getUserId();
//		timeStamp = user.getTimeStamp();
//		accessLevel = user.getAccessLevel();
//		nodes = user.getNodes();
//	}

//	public void init() {
//		name = mName;
//		type = mType;
//		userId = mUserId;
//		timeStamp = mTimeStamp;
//		accessLevel = mAccessLevel;
//	}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Message.UserType getType() {
	return type;
}

public void setType(Message.UserType type) {
	this.type = type;
}

public String getUserId() {
	return userId;
}

public void setUserId(String userId) {
	this.userId = userId;
}

public Date getTimeStamp() {
	return timeStamp;
}

public void setTimeStamp(Date timeStamp) {
	this.timeStamp = timeStamp;
}

public Constants.Access getAccessLevel() {
	return accessLevel;
}

public void setAccessLevel(Constants.Access accessLevel) {
	this.accessLevel = accessLevel;
}

//	public void init(User user) {
//		name = user.getName();
//		type = user.getType();
//		userId = user.getUserId();
//		timeStamp = user.getTimeStamp();
//		accessLevel = user.getAccessLevel();
//		nodes = user.getNodes();
//	}

	public ArrayList<User> convertUser(ArrayList<UserBundle> list) {
		ArrayList<User> users = new ArrayList<>();
		for (UserBundle b : list) {
			users.add(new User(b));
		}
		return users;
	}
@Override
public String toString() {
return ReflectionToStringBuilder.toString(this, MULTI_LINE_STYLE);
}
}
