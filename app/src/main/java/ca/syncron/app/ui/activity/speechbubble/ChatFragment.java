package ca.syncron.app.ui.activity.speechbubble;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import ca.syncron.app.R;
import ca.syncron.app.system.ottoevents.EEventBus;
import ca.syncron.app.system.ottoevents.EventBus;
import com.squareup.otto.Subscribe;
import org.androidannotations.annotations.*;
import roboguice.util.Ln;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
@EFragment(R.layout.fragment_chat)
public class ChatFragment extends Fragment {

	//	Context context = Syncron_.getInstance();


	@ViewById
	Button    sendMessage;
	@ViewById
	ListView  chatList;
	@Bean
	EEventBus bus;
//	@Bean
//	UserAdapter adapter;

	@AfterViews
	void bindAdapter() {
		bus.register(this);
		chatList.setAdapter(adapter);
		addNewMessage(new Message("Syncron Chat", true));
		//	userList.setAdapter(adapter);
	}

	@Click(R.id.sendMessage)
	void onSendMessage(View v) {
		sendMessage(v);
		bus.getInstance().newChatSendEvent("odroid", "msg Tester");
//		EventBus.getInstance().newChatSendEvent("odroid", "msg Tester");
	}
@UiThread
	@Subscribe
	public void onReceiveChat(EventBus.ChatReceiveEvent event) {
		Ln.d("RECEIVE CHAT EVENT");
		//Toast.makeText(Syncron.getInstance(), "Chat: " + event.mMsg, Toast.LENGTH_SHORT).show();
		//	mClient.sendChatMessage( Message.newChat(event.mMsg));

	addNewMessage(new Message(event.mUser.getUserId() + "\n" + event.mMsg,false));
	}

//	@Click(R.id.userButton)
//	void onClick(View v) {
//		userList.setAdapter(adapter);
//		UserBundle b = new UserBundle();
//		b.setName("TESSSSSSSSSSSSSSSSSSSSSSSSSSSSSST");
//	//	Toast.makeText(Syncron.getInstance(), SyncronService_.getInstance().getUserBundles().size(), Toast.LENGTH_SHORT).show();
//		SyncronService.getInstance().getUserBundles().add(b);
//		userList.invalidate();
//	}

	public ChatFragment() {
		// Required empty public constructor
	}

	//
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	ArrayList<Message> messages;
	AwesomeAdapter     adapter;
	@ViewById(R.id.text)
	EditText text;
	static Random rand = new Random();
	static String sender;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main_chatbubbles);

		//text = (EditText) this.findViewById(R.id.text);

//		sender = Utility.sender[rand.nextInt(Utility.sender.length - 1)];
//		this.setTitle(sender);
		messages = new ArrayList<Message>();

//		messages.add(new Message("Hello", false));
//		messages.add(new Message("Hi!", true));
//		messages.add(new Message("Wassup??", false));
//		messages.add(new Message("nothing much, working on speech bubbles.", true));
//		messages.add(new Message("you say!", true));
//		messages.add(new Message("oh thats great. how are you showing them", false));


		adapter = new AwesomeAdapter(getActivity(), messages);
		//chatList.setAdapter(adapter);
//		addNewMessage(new Message("mmm, well, using 9 patches png to show them.", true));
	}

	public void sendMessage(View v) {
		String newMessage = text.getText().toString().trim();
		if (newMessage.length() > 0) {
			text.setText("");
			addNewMessage(new Message(newMessage, true));
			//new SendMessage().execute();
		}
	}

	private class SendMessage extends AsyncTask<Void, String, String> {
		@Override
		protected String doInBackground(Void... params) {
//			try {
//				Thread.sleep(2000); //simulate a network call
//			}catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//			this.publishProgress(String.format("%s started writing", sender));
//			try {
//				Thread.sleep(2000); //simulate a network call
//			}catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			this.publishProgress(String.format("%s has entered text", sender));
//			try {
//				Thread.sleep(3000);//simulate a network call
//			}catch (InterruptedException e) {
//				e.printStackTrace();
//			}


			return Utility.messages[rand.nextInt(Utility.messages.length-1)];


		}
		@Override
		public void onProgressUpdate(String... v) {

			if(messages.get(messages.size()-1).isStatusMessage)//check wether we have already added a status message
			{
				messages.get(messages.size()-1).setMessage(v[0]); //update the status for that
				adapter.notifyDataSetChanged();
				chatList.setSelection(messages.size() - 1);
			}
			else{
				addNewMessage(new Message(true,v[0])); //add new message, if there is no existing status message
			}
		}
		@Override
		protected void onPostExecute(String text) {
			if(messages.get(messages.size()-1).isStatusMessage)//check if there is any status message, now remove it.
			{
				messages.remove(messages.size()-1);
			}

			addNewMessage(new Message(text, false)); // add the orignal message from server.
		}


	}
	void addNewMessage(Message m)
	{
		messages.add(m);
		adapter.notifyDataSetChanged();
		chatList.setSelection(messages.size() - 1);
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

