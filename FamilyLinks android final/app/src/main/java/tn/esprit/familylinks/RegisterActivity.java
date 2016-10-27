package tn.esprit.familylinks;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import tn.esprit.familylinks.Utils.sharedInformation;

public class RegisterActivity extends AppCompatActivity {
    private Uri imageCaptureUri;
    private ImageView mImageView;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    Button btn_choose;
    EditText country;
    EditText job;
    EditText lname;
    EditText fname;
    EditText username;
    EditText password;
    EditText verifPass;
    TextView dob;
    EditText email;
    Button signup;
    String usernametxt;
    String passwordtxt;
    String verifPasstxt;
    String gendertxt;
    String dobtxt;
    String jobtxt;
    String countrytxt;
    String lnametxt;
    String fnametxt;
    String emailtxt;
    Spinner spinner;
    Toolbar toolbar;
    ActionBar actionBar;
    ParseFile imgFile;
    ProgressDialog mProgressDialog;

    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mImageView = (ImageView) findViewById(R.id.img_show);
        ((Button) findViewById(R.id.btn_choose_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
            }
        });
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        //date picker
        dateView = (TextView) findViewById(R.id.dob);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
//spinner gender

        spinner = (Spinner) findViewById(R.id.Gender_Spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter1);


        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.Username);
        fname = (EditText) findViewById(R.id.FirstName);
        lname = (EditText) findViewById(R.id.LastName);
        password = (EditText) findViewById(R.id.password);
        verifPass = (EditText) findViewById(R.id.verifPassword);
        dob = (TextView) findViewById(R.id.dob);
        email = (EditText) findViewById(R.id.Email);
        country = (EditText) findViewById(R.id.Country);
        job = (EditText) findViewById(R.id.Job);
        signup = (Button) findViewById(R.id.sign_up_button);

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                //     return activeNetworkInfo != null && activeNetworkInfo.isConnected();
                if (activeNetworkInfo == null ){
                    Toast.makeText(RegisterActivity.this, "Verify your internet connection ", Toast.LENGTH_LONG).show();
                }else {
                    RegisterTask registerTask=new RegisterTask();
                    registerTask.execute();
                }
            }
        });
    }


    public class RegisterTask extends AsyncTask<Void, Integer, Void>
    {
        String control="No";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(RegisterActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Successfully add relation");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading Data");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
            // Retrieve the text entered from the EditText
            usernametxt = username.getText().toString();
            passwordtxt = password.getText().toString();
            verifPasstxt = verifPass.getText().toString();
            emailtxt = email.getText().toString();
            lnametxt = lname.getText().toString();
            fnametxt = fname.getText().toString();
            gendertxt = spinner.getSelectedItem().toString();
            dobtxt = dob.getText().toString();
            jobtxt = job.getText().toString();
            countrytxt = country.getText().toString();
            // Force user to fill up the form
            if (fnametxt.equals("")) {
                control="Please complete your name";
            } else if (lnametxt.equals("")) {
               // Toast.makeText(getApplicationContext(),"Please complete your last name",Toast.LENGTH_LONG).show();
                control="Please complete your last name";
            } else if (usernametxt.equals("")) {
               //Toast.makeText(getApplicationContext(),"Please complete your username",Toast.LENGTH_LONG).show();
                control="Please complete your username";
            }/* else if (emailtxt.equals("") && passwordtxt.equals("")) {
                        Toast.makeText(getApplicationContext(),
                                "Please complete your email or your password",
                                Toast.LENGTH_LONG).show();
                    }*/
            else if (country.getText().toString().equals("")) {
                //Toast.makeText(getApplicationContext(),"Please complete your country",Toast.LENGTH_LONG).show();
                control="Please complete your country";
            } else if (job.getText().toString().equals("")) {
                //Toast.makeText(getApplicationContext(),/"Please complete your job",/Toast.LENGTH_LONG).show();
                control="Please complete your job";
            } else if (imgFile == null) {
                //Toast.makeText(RegisterActivity.this, "plz add an image ", Toast.LENGTH_SHORT).show();
                control="Please add an image";
            } else if (!passwordtxt.equals(verifPasstxt)) {
                //Toast.makeText(RegisterActivity.this, "Verify your password ", Toast.LENGTH_SHORT).show();
                control="Verify your password ";
            } else {control="ok";}
        }

        @Override
        protected Void doInBackground(Void... arg0) {
        if(control=="ok") {
            // Save new user data into Parse.com Data Storage
            sharedInformation.sharedParseLastName = lnametxt;
            Log.e("lname", sharedInformation.sharedParseLastName);
            sharedInformation.sharedParseCountry = countrytxt;
            Log.e("countrytxt", sharedInformation.sharedParseCountry);
            final ParseUser user = new ParseUser();
            user.setUsername(usernametxt);
            user.setPassword(passwordtxt);
            user.setEmail(emailtxt);
            try {
                user.signUp();
            } catch (ParseException parseException) {
                control = parseException.getMessage();
                Log.e("control1111", control);
                Log.d("ex  :   ", parseException.getMessage());
                return null;
            }
            ParseObject Person = new ParseObject("Person");
            Person.put("User", user);
            Person.put("Email", emailtxt);
            Person.put("Gender", gendertxt);
            Person.put("DOB", dobtxt);
            Person.put("LastName", lnametxt);
            Person.put("FirstName", fnametxt);
            Person.put("Job", jobtxt);
            Person.put("Country", countrytxt);
            Person.put("image", imgFile);
            Person.put("FatherP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
            Person.put("MotherP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
            Person.put("SpouseP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
            try {
                Person.save();
            } catch (ParseException parseException) {
                control = parseException.getMessage();
                                /*Toast.makeText(getApplicationContext(),
                                        "Sign up Error: " + parseException.getMessage(), Toast.LENGTH_LONG)
                                        .show();*/
                Log.d("ex  :   ", parseException.getMessage().toString());
                return null;
            } finally {
                sharedInformation.sharedParsePersonId = Person.getObjectId();
                Log.w("person id registerrrrr", Person.getObjectId());
                control = "okok";
            }
            Log.e("control22222", control);
            //if(control.equals("ok"))
        }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Log.e("control",control);

                    if(control.equals("okok")){
                        Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Sign up Error: " + control, Toast.LENGTH_LONG)
                                .show();
                    }

            mProgressDialog.dismiss();
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        Bitmap bitmap = null;
        String path = "";

        if (requestCode == PICK_FROM_FILE) {
            imageCaptureUri = data.getData();
            path = getRealPathFromURI(imageCaptureUri); //from Gallery

            if (path == null)
                path = imageCaptureUri.getPath(); //from File Manager

            if (path != null)
                bitmap = BitmapFactory.decodeFile(path);
        } else {
            path = imageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }

        mImageView.setImageBitmap(bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //  bitmap.compress(Bitmap.CompressFormat.JPEG,20 , stream);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bitmap, 300, 300, false);
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        // get byte array here
        byte[] bytearray = stream.toByteArray();
        imgFile = new ParseFile("braveImg.jpg", bytearray);

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        if (cursor == null) return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}