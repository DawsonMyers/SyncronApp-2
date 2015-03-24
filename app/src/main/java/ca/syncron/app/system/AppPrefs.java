package ca.syncron.app.system;

import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Dawson on 3/23/2015.
 */
@SharedPref(value=SharedPref.Scope.APPLICATION_DEFAULT)
public interface AppPrefs{
	// The field name will have default value "John"
	@DefaultString("Android")
	String userName();

	// The field age will have default value 42
	@DefaultString("dawsonmyers.ca")
	String serverIp();
	@DefaultString("6500")
	String serverPort();

	// The field lastUpdated will have default value 0
	@DefaultLong(0)
	long lastUpdated();
}
