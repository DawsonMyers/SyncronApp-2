package ca.syncron.app.system;

import android.app.Application;
import android.content.Intent;
import org.androidannotations.annotations.EApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Dawson on 3/14/2015.
 */
@EApplication
public class Syncron extends Application {
	static String nameId = Syncron.class.getSimpleName();
	public static Syncron me;
	public ExecutorService          executor  = Executors.newCachedThreadPool();
	public ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
	SyncronService mService;

	public Syncron() {

	}

	public static Syncron getInstance() {
		return me;
	}

	public synchronized void setServiceRef(SyncronService service) {
		mService = service;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		me = this;

	}

	public void startService() {
		startService(new Intent(Syncron.this, SyncronService.class));
	}

	public void setPine(String pin, boolean value) {
		mService = SyncronService.getInstance();
		mService.setPin(pin, value);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		SyncronService_.intent(this).stop();
	}
}
