package tn.esprit.familylinks;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import tn.esprit.familylinks.Utils.sharedInformation;
import tn.esprit.familylinks.Utils.user;

/**
 * Created by L on 27-12-15.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button reg_button;
    private Button mEmailSignInButton;
    String usernametxt;
    String passwordtxt;
    Toolbar toolbar;
    ActionBar actionBar;
    public List<user> users;
    public user CPMother;
    public user CPFather;
    public user CPSpouse;
    public List<user> CPChildren;
    public List<user> CPBrother;
    ProgressDialog mProgressDialog;
    List<ParseObject> familyMember;
    List<ParseObject> allUser;
    List<ParseObject> fathers;

    List<ParseObject> Uncle1;
    List<ParseObject> Uncle2;

    public List<ParseObject> Uncles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedInformation.sharedBigger = new ArrayList<>();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        reg_button = (Button) findViewById(R.id.register_button);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                mEmailSignInButton.setEnabled(false);
                ConnectivityManager connectivityManager

                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                //     return activeNetworkInfo != null && activeNetworkInfo.isConnected();
                if (activeNetworkInfo == null ){
                    Toast.makeText(LoginActivity.this, "Verify your internet connection ", Toast.LENGTH_LONG).show();
                    mEmailSignInButton.setEnabled(true);
                }else {

                    // Retrieve the text entered from the EditText
                    usernametxt = mEmailView.getText().toString();
                    passwordtxt = mPasswordView.getText().toString();

                    // Send data to Parse.com for verification
                    ParseUser.logInInBackground(usernametxt, passwordtxt,
                            new LogInCallback() {
                                public void done(final ParseUser user, ParseException e) {
                                    if (user != null) {
                                        // If user exist and authenticated, send user to Welcome.class
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Person");
                                        query.whereEqualTo("User", user);
                                        query.include("FatherP");
                                        query.include("MotherP");
                                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                                            public void done(ParseObject CU, ParseException e) {
                                                if (CU == null) {
                                                    Log.d("score", "The getFirst request failed.");

                                                } else {
                                                    String personId = CU.getObjectId();
                                                    ParseFile file = CU.getParseFile("image");
                                                    String username = user.getString("Username");
                                                    String name = CU.getString("FirstName");
                                                    String mail = CU.getString("Email");
                                                    String lastName = CU.getString("LastName");
                                                    String job = CU.getString("Job");
                                                    String age = CU.getString("DOB");
                                                    String gender = CU.getString("Gender");
                                                    String country = CU.getString("Country");
                                                    sharedInformation.sharedParseFather = CU.getParseObject("FatherP");
                                                    sharedInformation.sharedParseMother = CU.getParseObject("MotherP");
                                                    sharedInformation.sharedParseSpouse = CU.getParseObject("SpouseP");
                                                    String fatherId = CU.getParseObject("FatherP").getObjectId();
                                                    String MotherId = CU.getParseObject("MotherP").getObjectId();
                                                    String SpouseId = CU.getParseObject("SpouseP").getObjectId();

                                                    Bitmap bitmap = null;
                                                    try {
                                                        InputStream is = new FileInputStream(file.getFile());
                                                        bitmap = BitmapFactory.decodeStream(is);
                                                    } catch (FileNotFoundException ex) {
                                                        ex.printStackTrace();
                                                    } catch (ParseException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                    user cu = new user(personId, username, mail, name, lastName, gender,
                                                            age, job, country, bitmap, fatherId, MotherId, SpouseId);

                                                    sharedInformation.sharedcurrentPerson = cu;
                                                    //**************************************Load family data****************************************
                                                    LoadFamily fami = new LoadFamily();
                                                    fami.execute();
                                                    mEmailSignInButton.setEnabled(true);
                                                }
                                            }
                                        });
                                        // finish();
                                    } else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Verify your Username/Password",
                                                Toast.LENGTH_LONG).show();
                                        mEmailSignInButton.setEnabled(true);
                                    }
                                }
                            });
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        reg_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                reg_button.setEnabled(false);
                ConnectivityManager connectivityManager

                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                //     return activeNetworkInfo != null && activeNetworkInfo.isConnected();
                if (activeNetworkInfo == null ){
                    Toast.makeText(LoginActivity.this, "Verify your internet connection ", Toast.LENGTH_LONG).show();
                    reg_button.setEnabled(true);
                }else {
                    Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    reg_button.setEnabled(true);
                    finish();
                }
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            Intent login=new Intent(LoginActivity.this, MainActivity.class);
            startActivity(login);
            mAuthTask.execute((Void) null);
            finish();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

/*
    public class LoadAllUser extends AsyncTask<Void, Integer, Void>
    {
        int count = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Successfully Logged in");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading Data");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //initializeFamily();
            initializeData();
            while (count < 5) {
                SystemClock.sleep(1000); count++;
                publishProgress(count * 20);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            mProgressDialog.dismiss();
        }
    }
    */

    public class LoadFamily extends AsyncTask<Void, Integer, Void> {
        private List<user> familysUser = new ArrayList<>();//will delete when configure save in phone
        private List<ParseObject> familysParsePersons = new ArrayList<>();//will delete when configure save in phone
        private List<ParseObject> familysParsePersonsSave = new ArrayList<>();//will delete when configure save in phone
        private List<ParseObject> generations;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Successfully Logged in");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading Data");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            users=new ArrayList<>();
            Thread timerThread = new Thread(){
                public void run(){
                    try{
                        ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("Person");
                        allUser= queryAll.find();
                        for (ParseObject j : allUser) {
                            if(j.getObjectId().equals("RyQlY5U6UM"))sharedInformation.sharedParseUnknown=j;
                            else{
                                ParseFile file = j.getParseFile("image");
                                String username = j.getString("Username");
                                String name = j.getString("FirstName");
                                String mail = j.getString("Email");
                                String lastName = j.getString("LastName");
                                String job = j.getString("Job");
                                String age = j.getString("DOB");
                                String gender = j.getString("Gender");
                                String country = j.getString("Country");
                                String personId = j.getObjectId();
                                Bitmap bitmap = null;
                                try {
                                    InputStream is = new FileInputStream(file.getFile());
                                    bitmap = BitmapFactory.decodeStream(is);
                                } catch (FileNotFoundException ex) {
                                    ex.printStackTrace();
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                }
                                user u = new user(personId, username, mail, name, lastName, gender, age, job, country, bitmap,
                                        j.getParseObject("FatherP").getObjectId(),
                                        j.getParseObject("MotherP").getObjectId(),
                                        j.getParseObject("SpouseP").getObjectId());
                                users.add(u);
                                //Log.d("getUsername in ", U.getString("LastName"));
                            }
                            sharedInformation.sharedUsers=users;
                        }
                        Log.e("splash list size",Integer.toString(sharedInformation.sharedUsers.size()));
                    } catch (ParseException e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                }
            };
                timerThread.start();

            /*Uncle1 = new ArrayList<ParseObject>();

            Uncle1.add(sharedInformation.sharedParseFather);
            //Uncle1.add(sharedInformation.sharedParseMother);
            familysParsePersons = sharedInformation.sharedBigger;
            //**************************Uncle********************************************
            for (ParseObject j : Uncle1) {
                Log.e("parent", j.getObjectId());
                try {
                    if (!j.getObjectId().equals("RyQlY5U6UM")) {
                        //**************************grand father********************************************
                        if (!familysParsePersons.contains(j)) {
                            familysParsePersons.add(j);
                            Log.e("Uncle1111", Integer.toString(familysParsePersons.size()));
                        }
                        if (!j.getParseObject("FatherP").getObjectId().equals("RyQlY5U6UM")) {
                            Log.e("Uncle222", Integer.toString(familysParsePersons.size()));
                            ParseQuery<ParseObject> queryUncle = ParseQuery.getQuery("Person");
                            queryUncle.whereEqualTo("FatherP", j.getParseObject("FatherP"));
                            familyMember = queryUncle.find();
                            if (familyMember.size() > 0) {
                                for (ParseObject Uncle : familyMember) {
                                    if (!familysParsePersons.contains(Uncle)) {
                                        familysParsePersons.add(Uncle);
                                        Log.e("Uncle", Uncle.getObjectId());
                                    }
                                }
                                Log.e("familysParsePerson0", Integer.toString(familysParsePersons.size()));
                            }
                        }
                    }

                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                Log.e("familysParsePerson1", Integer.toString(familysParsePersons.size()));
            }
            sharedInformation.sharedBigger = familysParsePersons;
            Log.e("familysParsePerson2", Integer.toString(familysParsePersons.size()));
            Log.e("size sharedBigger0", Integer.toString(sharedInformation.sharedBigger.size()));
            Log.e("familysParsePerson3333", Integer.toString(familysParsePersons.size()));
            familysParsePersons = sharedInformation.sharedBigger;
            familysParsePersonsSave = familysParsePersons;
            Log.e("list", familysParsePersons.toString());

            //**************************generation********************************************
            for (ParseObject uncle : familysParsePersons) {
                Log.e("size sharedBigger start", Integer.toString(sharedInformation.sharedBigger.size()));
                familysParsePersonsSave = sharedInformation.sharedBigger;
                generations = new ArrayList<ParseObject>();
                try {
                    Log.e("uuuncle", uncle.getObjectId());
                    ParseQuery<ParseObject> queryGeneration = new ParseQuery<ParseObject>("Person");
                    queryGeneration.whereEqualTo("FatherP", uncle);
                    queryGeneration.include("FatherP");
                    generations = queryGeneration.find();
                } catch (ParseException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                Log.e("sizezzz", Integer.toString(generations.size()));
                if (generations.size() > 0) {
                    for (ParseObject Uncle : generations) {
                        Log.e("Uncle2", Uncle.getObjectId());
                        if (!familysParsePersonsSave.contains(Uncle)) {
                            familysParsePersonsSave.add(Uncle);
                            Log.e("new", Uncle.getObjectId());
                        } else {
                            Log.e("old", Uncle.getObjectId());
                        }
                    }
                    Log.e("size sharedBigger2", Integer.toString(familysParsePersonsSave.size()));
                    sharedInformation.sharedBigger = familysParsePersonsSave;
                    Log.e("size sharedBigger2", Integer.toString(sharedInformation.sharedBigger.size()));
                }
                Log.e("list1", familysParsePersonsSave.toString());
                Log.e("list2", sharedInformation.sharedBigger.toString());
            }

            Log.e("familysParsePerson2", Integer.toString(familysParsePersons.size()));
            Log.e("familysParsePersonsSave", Integer.toString(familysParsePersonsSave.size()));
            familysParsePersons = familysParsePersonsSave;


            Log.e("Bigger out 2", Integer.toString(sharedInformation.sharedBigger.size()));
            for (ParseObject pers : sharedInformation.sharedBigger) {
                ParseFile file = pers.getParseFile("image");
                String username = pers.getString("Username");
                String name = pers.getString("FirstName");
                String mail = pers.getString("Email");
                String lastName = pers.getString("LastName");
                String job = pers.getString("Job");
                String country = pers.getString("Country");
                String age = pers.getString("DOB");
                String gender = pers.getString("Gender");
                String personId = pers.getObjectId();
                Log.d("personId", personId);
                Bitmap bitmap = null;
                try {
                    InputStream is = new FileInputStream(file.getFile());
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                user u = new user(personId, username, mail, name, lastName, gender, age, job, country, bitmap,
                        pers.getParseObject("FatherP").getObjectId(),
                        pers.getParseObject("MotherP").getObjectId(),
                        pers.getParseObject("SpouseP").getObjectId());
                if (!familysUser.contains(u)) {
                    familysUser.add(u);
                }
            }

            sharedInformation.sharedFamily = familysUser;
            Log.e("familys", Integer.toString(familysUser.size()));*/

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            mProgressDialog.dismiss();
            mEmailSignInButton.setEnabled(true);
            finish();
            //Toast.makeText(getApplicationContext(), "Le traitement asynchrone est terminé", Toast.LENGTH_LONG).show();
        }
    }
}