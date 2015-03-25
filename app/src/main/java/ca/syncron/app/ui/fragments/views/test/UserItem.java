package ca.syncron.app.ui.fragments.views.test;

import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/22/2015.
 */
public class UserItem {
	static              String           nameId = UserItem.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);

	//	public Person() {}
	public final String userId;
	public final String timestamp;
	public final String type;

	public UserItem(String userId, String timestamp, String type) {
		this.userId = userId;
		this.timestamp = timestamp;
		this.type = type;
	}

}
