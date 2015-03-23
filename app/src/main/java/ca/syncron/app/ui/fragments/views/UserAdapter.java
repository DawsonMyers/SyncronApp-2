package ca.syncron.app.ui.fragments.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import ca.syncron.app.network.UserBundle;
import ca.syncron.app.network.connection.User;
import ca.syncron.app.system.SyncronService;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Dawson on 3/22/2015.
 */
@EBean
public class UserAdapter extends BaseAdapter {
	static              String                nameId       = UserAdapter.class.getSimpleName();
	public final static org.slf4j.Logger      log          = LoggerFactory.getLogger(nameId);
	public              ArrayList<UserBundle> mUserBundles = SyncronService.getInstance().getUserBundles();
	;
	public ArrayList<User> users = new ArrayList<>();

//	public ArrayList<UserBundle> getUsers() {
//		return users;
//	}
//
//	public void setUsers(ArrayList<UserBundle> users) {
//		this.users = users;
//	}

	//public ArrayList<UserBundle> users = SyncronService.getInstance().getUserBundles();
	@RootContext
	Context context;

	public UserAdapter() {}

	@AfterInject
	void initAdapter() {
		users = (ArrayList<User>) SyncronService.getInstance().getUsers().clone();
		users = users;//personFinder.findAll();
//		users.add(new UserBundle());
//		users.add(new UserBundle());
//		users.add(new UserBundle());


	}

	@Override
	public int getCount() {
		return 0;// users.size();
	}

	@Override
	public User getItem(int position) {
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserItemView uView;
		if (convertView == null) {
			uView = UserItemView_.build(context);
		} else {
			uView = (UserItemView) convertView;
		}

		uView.bind(getItem(position));

		return uView;

	}
}
