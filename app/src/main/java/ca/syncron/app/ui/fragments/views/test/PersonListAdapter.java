package ca.syncron.app.ui.fragments.views.test;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import ca.syncron.app.network.connection.User;
import ca.syncron.app.system.SyncronService;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawson on 3/22/2015.
 */
@EBean
public class PersonListAdapter extends BaseAdapter {
	public List<User>   user   = new ArrayList<>();
	public List<Person> person = new ArrayList<>();
	List<Person> persons = new ArrayList<>();
	;

//	@Bean(InMemoryPersonFinder.class)
//	PersonFinder personFinder;

	@RootContext
	Context context;

	@AfterInject
	void initAdapter() {
		persons = persons;//personFinder.findAll();
		user = SyncronService.getInstance().getUsers();//personFinder.findAll();
		for (User u : user) {
			persons.add(new Person(u.getTimeStamp(),u.getUserId(), u.getType() ));
		}
//		persons.add(new Person("Dawson", "Myers"));
//		persons.add(new Person("Shea", "Myers"));
//		persons.add(new Person("Bob", "Coach"));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

//		UserItemView userItemView;
		PersonItemView personItemView;
		if (convertView == null) {
//			userItemView = ca.syncron.app.ui.fragments.views.UserItemView_.build(context);
			personItemView = PersonItemView_.build(context);
		} else {
//			userItemView = (UserItemView) convertView;
			personItemView = (PersonItemView) convertView;
		}

//		userItemView.bind(getItem(position));
		personItemView.bind(getItem(position));

//		return userItemView;
		return personItemView;
	}

	@Override
	public int getCount() {
		return persons.size();
	}

	@Override
	public Person getItem(int position) {
//	public Person getItem(int position) {
//		return user.get(position);
		return persons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
