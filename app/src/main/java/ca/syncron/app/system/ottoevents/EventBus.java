package ca.syncron.app.system.ottoevents;

import ca.syncron.app.network.connection.User;
import ca.syncron.app.network.Message;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Dawson on 3/23/2015.
 */
// @EBean(scope = EBean.Scope.Singleton)
@Singleton
public class EventBus {
	static              String           nameId = EventBus.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);

	//@Inject
	public EventBus() {}

	private static final Bus      BUS  = new Bus(ThreadEnforcer.ANY);
	private static final EventBus eBus = new EventBus();

	@Inject
	public static EventBus getInstance() {
		return eBus;
	}

	public static Bus getBus() {
		return BUS;
	}

	public static void post(Object obj) {
		BUS.post(obj);
	}

	public static void register(Object object) {
		BUS.register(object);
	}
//  Builders
///////////////////////////////////////////////////////
	public void newUpdateUserEvent(ArrayList<User> users) {
		getInstance().post(new UpdateUserEvent(users));
	}

	public void newChatSendEvent(String userId, String msg) {
		getBus().post(new ChatSendEvent(userId, msg));
	}

	public void newChatReceiveEvent(User user, String msg) {
		getInstance().post(new ChatReceiveEvent(user, msg));
	}

	public void newSendTargetEvent(String userId, Message msg) {
		getInstance().post(new SendTargetEvent(userId, msg));
	}

	public void newProgressEvent(boolean showProgress) {
		getInstance().post(new ProgressEvent(showProgress));
	}
	public void newSetTargetIdEvent(String targetId) {
		getInstance().post(new SetTargetIdEvent(targetId));
	}


	//  Events
//////////////////////////////////////////////////////////////////////////////////////////////////////////
	public class ProgressEvent {
		public final boolean showProgress;

		public ProgressEvent(boolean b) {
			showProgress = b;
		}
	}

	//
///////////////////////////////////////////////////////
	public class ChatReceiveEvent {
		public final User   mUser;
		public final String mMsg;

		public ChatReceiveEvent(User user, String msg) {
			mUser = user;
			mMsg = msg;
		}
	}

	//
///////////////////////////////////////////////////////
	public class ChatSendEvent {
		public final String mUserId;
		public final String mMsg;

		public ChatSendEvent(String userId, String msg) {
			mUserId = userId;
			mMsg = msg;
		}
	}

	//
///////////////////////////////////////////////////////
	public class SendTargetEvent {
		public final String  userId;
		public final Message msg;


		public SendTargetEvent(String userId, Message msg) {
			this.userId = userId;
			this.msg = msg;
		}
	}

	//
///////////////////////////////////////////////////////
	public class UpdateUserEvent {
		public final ArrayList<User> mUsers;

		public UpdateUserEvent(ArrayList<User> users) {
			mUsers = users;
		}
	}

	//
///////////////////////////////////////////////////////
	public class SetTargetIdEvent {
		public String targetId;

		public SetTargetIdEvent(String targetId) {
			this.targetId = targetId;
		}
	}

}
