package ca.syncron.app.system.ottoevents;

import ca.syncron.app.network.connection.User;
import ca.syncron.app.ui.activity.speechbubble.Message;
import com.google.inject.Inject;
import com.squareup.otto.Bus;
import org.androidannotations.annotations.EBean;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Dawson on 3/23/2015.
 */
 @EBean(scope = EBean.Scope.Singleton)
public class EEventBus {
	static              String           nameId = EEventBus.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);
@Inject EventBus eventBus;
	public EEventBus() {}

	private static final Bus       BUS  = EventBus.getBus();//new Bus();
	private static final EventBus eBus = EventBus.getInstance(); //new EEventBus();

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

	public void newUserUpdateEvent(ArrayList<User> users) {
		eBus.newUpdateUserEvent(users);
//		getInstance().post(new EventBus.UpdateUserEvent(users));

	}

	public void newSendChatEvent(String userId, String msg) {
		eBus.newChatSendEvent(userId, msg);
//		getBus().post(new EventBus.ChatSendEvent(userId, msg));
	}

	public void newReceiveChatEvent(User user, String msg) {
		eBus.newChatReceiveEvent(user, msg);
//		getInstance().post(new ChatReceiveEvent(user, msg));
	}
	public void newSendTargetEvent(String userId,Message msg) {
		eBus.newSendTargetEvent(  userId,  msg);
//		getInstance().post(eBus.newSendTargetEvent(  userId,  msg));
	}
	public void newProgressEvent(boolean showProgress) {
		eBus.newProgressEvent(showProgress);
//		getInstance().post(new ProgressEvent(showProgress));
	}

	//  Events
	///////////////////////////////////////////////////////
//	public class ProgressEvent {
//		public final boolean showProgress;
//
//		public ProgressEvent(boolean b) {
//			showProgress = b;
//		}
//	}
//	public class ChatReceiveEvent {
//		public final User mUser;
//		public final String mMsg;
//
//		public ChatReceiveEvent(User user,String msg) {
//			mUser = user;
//			mMsg = msg;
//		}
//	}
//	public class ChatSendEvent {
//		public final String  mUserId;
//		public final String mMsg;
//
//		public ChatSendEvent(String userId,String msg) {
//			mUserId = userId;
//			mMsg = msg;
//		}
//	}
//	public class SendTargetEvent {
//		public final String  mUserId;
//		public final Message mMsg;
//
//
//		public SendTargetEvent(String userId,Message msg) {
//			mUserId = userId;
//			mMsg = msg;
//		}
//	}
//
//
//		public class UpdateUserEvent {
//		public final ArrayList<User> mUsers;
//
//		public UpdateUserEvent(ArrayList<User> users) {
//			mUsers = users;
//		}
//	}
}
