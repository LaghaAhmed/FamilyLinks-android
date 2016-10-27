package tn.esprit.familylinks.Utils;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
/**
 * Created by L on 27-12-15.
 */
public class ParseApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
// Enable Local Datastore.
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);


        Parse.initialize(this, "NULJ74YbTTSQNw3kLlgchZngdINgb8cDfcOMqEAh", "ip5rrLANHQ5vzCMno73JR1j19ANXLBHjQDnV6ekt");

        //   ParseObject.registerSubclass();
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}