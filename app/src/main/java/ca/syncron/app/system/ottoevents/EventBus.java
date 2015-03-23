package ca.syncron.app.system.ottoevents;

import ca.syncron.app.network.connection.User;
import com.squareup.otto.Bus;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Dawson on 3/23/2015.
 */
//@EBean(scope = EBean.Scope.Singleton)
public class EventBus {
	static              String           nameId = EventBus.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);

	public EventBus() {}

	private static final Bus BUS = new Bus();

	public static Bus getInstance() {
		return BUS;
	}

	public static void register(Object object) {
		BUS.register(object);
	}
	public   void newUserUpdateEvent(ArrayList<User> users) {
		getInstance().post(new UpdateUserEvent(users));
	}
	public void newProgressEvent(boolean showProgress) {
		getInstance().post(new ProgressEvent(showProgress));
	}

	//  Events
	///////////////////////////////////////////////////////
	public class ProgressEvent {
		public final boolean showProgress;

		public ProgressEvent(boolean b) {
			showProgress = b;
		}
	}


		public class UpdateUserEvent {
		public final ArrayList<User> mUsers;

		public UpdateUserEvent(ArrayList<User> users) {
			mUsers = users;
		}
	}
}
