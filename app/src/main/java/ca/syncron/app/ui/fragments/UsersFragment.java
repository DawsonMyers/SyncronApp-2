package ca.syncron.app.ui.fragments;


import android.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import ca.syncron.app.R;
import ca.syncron.app.system.ottoevents.EEventBus;
import ca.syncron.app.ui.fragments.views.UserAdapter;
import ca.syncron.app.ui.fragments.views.test.PersonListAdapter;
import ca.syncron.app.ui.fragments.views.test.UserItem;
import org.androidannotations.annotations.*;

/**
 * A simple {@link android.app.Fragment} subclass.
 *
 */
@EFragment(R.layout.fragment_help)
public class UsersFragment extends Fragment {
@ViewById
	Button button;
	@ViewById
	ListView personList;

	public UsersFragment() {
		// Required empty public constructor
	}

	@Bean
	PersonListAdapter adapter;
	@Bean
	UserAdapter       adapter1;
@Bean
	EEventBus bus;
	@AfterViews
	void bindAdapter() {
		personList.setAdapter(adapter);
	}

	@Click(R.id.button)
	void onButtonClick(View v) {
		personList.setAdapter(adapter);
	}

	@ItemClick
	void personListItemClicked(UserItem userItem) {
bus.newSetTargetIdEvent(userItem.userId);
	//	Toast.makeText(this.getActivity(), userItem.firstName + " " + userItem.lastName, LENGTH_LONG).show();
	}

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//	                         Bundle savedInstanceState) {
//		// Inflate the layout for this fragment
//		return inflater.inflate(R.layout.fragment_help, container, false);
//	}

}
