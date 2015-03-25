package ca.syncron.app.ui.fragments.views.test;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.syncron.app.R;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Dawson on 3/22/2015.
 */
@EViewGroup(R.layout.person_item)
public class PersonItemView extends LinearLayout {

	@ViewById
	TextView firstNameView;

	@ViewById
	TextView lastNameView;

	public PersonItemView(Context context) {
		super(context);
	}

	public void bind(UserItem userItem) {
		firstNameView.setText(userItem.firstName);
		lastNameView.setText(userItem.lastName);
	}
}
