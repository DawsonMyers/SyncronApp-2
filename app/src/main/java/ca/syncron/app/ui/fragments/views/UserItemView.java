package ca.syncron.app.ui.fragments.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.syncron.app.R;
import ca.syncron.app.network.connection.User;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.ViewById;
import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/22/2015.
 */
@EViewGroup(R.layout.user_item1)
public class UserItemView extends LinearLayout {
	static              String           nameId = UserItemView.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);


	@ViewById
	TextView uName;
	@ViewById
	TextView uType;
	@ViewById
	TextView uId, uDate, uAccess;

	public UserItemView(Context context) {
		super(context);
	}
//
 	@Trace
 	public void bind(User bundle) {
		uName.setText(bundle.getName());
		uType.setText(bundle.getType().toString());
		uId.setText(bundle.getUserId());
		uDate.setText(bundle.getTimeStamp().toString());
		uAccess.setText(bundle.getAccessLevel().toString());
		log.error("USER ITEM BOUND");
 	}

//	@Trace
//			public void bind(UserBundle bundle) {
//			uName.setText("Name");
//			uType.setText("Type");
//			uId.setText("ID");
//			uDate.setText("Date");
//			uAccess.setText("Access");
//		}
}

