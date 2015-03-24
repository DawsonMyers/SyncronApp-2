package ca.syncron.app.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import ca.syncron.app.R;
import ca.syncron.app.system.SyncronService;
import ca.syncron.app.system.SyncronService_;
import ca.syncron.app.ui.activity.speechbubble.ChatFragment_;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

//@EActivity(R.layout.activity_fragment)
@EActivity
public class FragmentActivity extends Activity implements SyncronService.UpdateObserver, SyncronService.ConnectionObserver{

@StringArrayRes(R.array.navigation_drawer_items_array)
      String[]     mNavigationDrawerItemTitles1;
	String[]     mNavigationDrawerItemTitles = new String[]{"Users", "Connection", "Status", "Chat"};
	@ViewById(R.id.drawer_layout)
      DrawerLayout mDrawerLayout;
	@ViewById(R.id.left_drawer)
      ListView     mDrawerList;


	//****  Have to update index when adding new fragments
    ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_fragment);

       // mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
      //  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
     //   mDrawerList = (ListView) findViewById(R.id.left_drawer);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);

        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_action_copy, "Users");
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_action_refresh, "Connection");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_action_share, "Status");
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_action_share, "Chat");

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

	@Override
	public void connectionStatus(boolean b) {

	}

	@Override
	public void updateAnalog(int[] values) {

	}

	@Override
	public void updateDigital(int[] values) {

	}

	@Override
	public void updateChat(String[] values) {

	}


	public class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new UsersFragment_.FragmentBuilder_().build();
                break;
            case 1:
                fragment = new ConnectionFragment_.FragmentBuilder_().build();
                break;
            case 2:
                fragment = new HelpFragment_.FragmentBuilder_().build();
                break;
            case 3:
            fragment = new ChatFragment_.FragmentBuilder_().build();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
           // getActionBar().setTitle(mNavigationDrawerItemTitles[position]);
            getActionBar().setTitle(drawerItem[position].name);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frag, menu);
        return super.onCreateOptionsMenu(menu); //true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
	    switch (item.getItemId()) {

		    case R.id.action_connect:
			    SyncronService_.intent(getApplication()).start();
	    }
        return super.onOptionsItemSelected(item);
    }
}
