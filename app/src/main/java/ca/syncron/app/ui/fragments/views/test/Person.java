package ca.syncron.app.ui.fragments.views.test;

import org.slf4j.LoggerFactory;

/**
 * Created by Dawson on 3/22/2015.
 */
public class Person {
	static              String           nameId = Person.class.getSimpleName();
	public final static org.slf4j.Logger log    = LoggerFactory.getLogger(nameId);

//	public Person() {}
	public final String firstName;
	public final String lastName;
	public final String type;

	public Person(String firstName, String lastName, String type) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.type = type;
	}



}
