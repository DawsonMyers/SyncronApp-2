package ca.syncron.app.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ca.syncron.app.R;
import ca.syncron.app.system.AppPrefs_;
import ca.syncron.app.system.Syncron_;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
@EFragment(R.layout.fragment_connection)
public class ConnectionFragment extends Fragment {

	Context context = Syncron_.getInstance();
	@ViewById
	Button   saveButton;
	@ViewById
	EditText serverIp, serverPort, userName;
	//	@Bean
//	UserAdapter adapter;
	@Pref
	AppPrefs_ prefs;
	public android.os.Handler handler = new android.os.Handler();

	@UiThread
	@AfterViews
	void init() {

		try {
			serverIp.setHint(prefs.serverIp().get());
			serverPort.setHint(prefs.serverPort().get());
		} catch (Exception e) {
			e.printStackTrace();
		}
		userName.setHint(prefs.userName().get());
	}

	@Click(R.id.saveButton)
	void onClick(View v) {
		if (serverIp.getText().length() > 0) prefs.edit().serverIp().put(serverIp.getText().toString()).apply();
		if (serverPort.getText().length() > 0) prefs.edit().serverPort().put(serverPort.getText().toString()).apply();
		if (userName.getText().length() > 0) prefs.edit().userName().put(userName.getText().toString()).apply();
	}

	public ConnectionFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

