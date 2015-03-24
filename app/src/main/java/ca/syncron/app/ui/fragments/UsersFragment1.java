package ca.syncron.app.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import ca.syncron.app.R;
import ca.syncron.app.network.UserBundle;
import ca.syncron.app.system.Syncron;
import ca.syncron.app.system.SyncronService;
import ca.syncron.app.system.Syncron_;
import ca.syncron.app.ui.fragments.views.UserAdapter;
import org.androidannotations.annotations.*;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
@EFragment(R.layout.fragment_users)
public class UsersFragment1 extends Fragment {

	Context context = Syncron_.getInstance();
	@ViewById
	Button userButton;
	@ViewById
	ListView userList;
	@Bean
	UserAdapter adapter;
	@AfterViews
	void bindAdapter() {
		userList.setAdapter(adapter);
	}
	@Click(R.id.userButton)
	void onClick(View v) {
		userList.setAdapter(adapter);
		UserBundle b = new UserBundle();
		b.setName("TESSSSSSSSSSSSSSSSSSSSSSSSSSSSSST");
	//	Toast.makeText(Syncron.getInstance(), SyncronService_.getInstance().getUserBundles().size(), Toast.LENGTH_SHORT).show();
		SyncronService.getInstance().getUserBundles().add(b);
		userList.invalidate();
	}

	public UsersFragment1() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}






	@ItemClick
	void userListItemClicked(UserBundle user) {
		Toast.makeText(Syncron.getInstance(), user.getName() + " " +user.getUserId(), Toast.LENGTH_SHORT).show();
	}

}
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_users, container, false);
//    }
//@EViewGroup(R.layout.user_item)
//class UserItemView extends RelativeLayout{
//@ViewById
//	TextView uName, uType, uId, uDate, uAccess;
//	public UserItemView(Context context) {
//		super(context);
//	}
//
//	public void bind(Message.UserBundle bundle) {
//	 uName.setText(bundle.getName());
//	 uType.setText(bundle.getType().toString());
//	 uId.setText(bundle.getUserId());
//	 uDate.setText(bundle.getTimeStamp().toString());
//	 uAccess.setText(bundle.getAccessLevel().toString());
//	}
//}

