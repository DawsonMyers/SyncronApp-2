package ca.syncron.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import ca.syncron.app.system.SyncronService;
import ca.syncron.app.system.SyncronService_;
import ca.syncron.app.ui.fragments.FragmentActivity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.androidannotations.annotations.*;

@EActivity
public class MainActivity extends Activity implements SyncronService.UpdateObserver, SyncronService.ConnectionObserver {
	static int count = 0;
	Handler handler = new Handler();
	@ViewById
	Button   b1;
	@ViewById
	Button   b2;


	@ViewById
	TextView tv1;
	@ViewById
	TextView txtAnalog1;
//	@ViewById
//	TextView tv3;
	@ViewById
	TextView tvCon;
	@ViewById
	ToggleButton bDigital1;
	@ViewById
	ToggleButton bDigital2;
	@ViewById
	ToggleButton bDigital3;
	@ViewById
	ToggleButton bDigital4;
	@ViewById
	ToggleButton bDigital5;
	@ViewById
	ToggleButton bDigital6;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SyncronService.observeAnalog(this, true);
		SyncronService.observeDigital(this, true);
		SyncronService.observeConnection(this, true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		SyncronService.observeAnalog(this, false);
		SyncronService.observeDigital(this, false);
		SyncronService.observeConnection(this, true);
	}
@UiThread
	@Override
	public void updateAnalog(int[] values) {
//		count++;
//		handler.post(() -> {
//			if (values != null) {
//				txtAnalog1.setText(values[2]+"");
//			}
//			//updateCount();
//		});

	}
	@UiThread
	@Override
	public void updateDigital(int[] values) {
//		count++;
//		handler.post(() -> {
//			bDigital1.setChecked(bool(values[0]) );
//			bDigital2.setChecked(bool(values[1]) );
//			bDigital3.setChecked(bool(values[2]) );
//			bDigital4.setChecked(bool(values[3]) );
//			bDigital5.setChecked(bool(values[4]) );
//
//			//updateCount();
//		});
	}

	public boolean bool(int val) {
		return val == 1 ? true : false;
	}
@UiThread
	public void updateCount() {
	Toast.makeText(getApplicationContext(), "Count = " + count, Toast.LENGTH_SHORT).show();
//	tv3.setText(count);
}
	@UiThread
	@Override
	public void updateChat(String[] values) {

	}
	@UiThread
	@Override
	public void connectionStatus(boolean b) {
		//handler.post(() -> {
			if (b) {
				tvCon.setTextColor(Color.GREEN);
				tvCon.setText("Connected");
			} else {
				tvCon.setTextColor(Color.RED);
				tvCon.setText("Disconnected");
			}
			updateCount();
		//});
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	class Box {
		@JsonCreator
		Box() {
		}

		int length = 10;
		int height = 20;

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}
	}

	@Click(R.id.b1)
	void onClick(View v) {
		startActivity(new Intent(MainActivity.this, FragmentActivity.class));
//		MessageProcessor  mapper = MessageProcessor.newProcessor();
//		String s = mapper.serializeMessage(new Box());
//		Log.d("json", s);
		//tv1.setText(s);
	}

	@Click(R.id.b2)
	void onServicClick(View v) {
		SyncronService_.intent(getApplication()).start();
	//	Syncron.getInstance().startService();
	}
	@Click(R.id.bDigital1)
	void onD1Click(View v) {SyncronService_.getInstance().sendDigital("2", bDigital1.isChecked());
	}
	@Click(R.id.bDigital2)
	void onD2Click(View v) {
		SyncronService_.getInstance().sendDigital("3", bDigital2.isChecked());
	}
	@Click(R.id.bDigital3)
	void onD3Click(View v) {
		SyncronService_.getInstance().sendDigital("4", bDigital3.isChecked());
	}
	@Click(R.id.bDigital4)
	void onD4Click(View v) {
		SyncronService_.getInstance().sendDigital("5", bDigital4.isChecked());
	}
	@Click(R.id.bDigital5)
	void onD5Click(View v) {
		SyncronService_.getInstance().sendDigital("6", bDigital5.isChecked());
	}
	@Background
	@Click(R.id.bDigital6)
	void onD6Click(View v) {
		SyncronService_.getInstance().sendStream(5000, true);
	}
	@Background
	@Click(R.id.tvCon)
	void onConnectionClick(View v) {
		SyncronService_.getInstance().mClient.connect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
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

		return super.onOptionsItemSelected(item);
	}
}
