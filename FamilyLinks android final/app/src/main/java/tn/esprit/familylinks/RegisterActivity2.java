package tn.esprit.familylinks;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
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

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import tn.esprit.familylinks.Utils.sharedInformation;

public class RegisterActivity2 extends AppCompatActivity {
    private Uri imageCaptureUri;
    private ImageView mImageView;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    EditText country;
    EditText job;
    EditText lname ;
    EditText fname;
    TextView dob;
    Button signup;
    String dobtxt;
    String jobtxt;
    String lnametxt;
    String fnametxt;
    Spinner spinner;
    Toolbar toolbar;
    ActionBar actionBar;
    ParseFile imgFile;

    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    String sharedCountry;
    String sharedPersonId;
    String sharedLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        sharedCountry=sharedInformation.sharedParseCountry;
        sharedPersonId=sharedInformation.sharedParsePersonId;
        sharedLastName=sharedInformation.sharedParseLastName;

        mImageView = (ImageView) findViewById(R.id.img_showf);
        ((Button) findViewById(R.id.btn_choose_imageF)).setOnClickListener(new View.OnClickListener() {
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
        dateView = (TextView) findViewById(R.id.dobf);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        // Locate EditTexts in main.xml
        fname = (EditText) findViewById(R.id.FirstNameFather);
        //country = (EditText) findViewById(R.id.Country);
        job = (EditText) findViewById(R.id.JobFather);
        signup = (Button)findViewById(R.id.addfath);

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
                    Toast.makeText(RegisterActivity2.this, "Verify your internet connection ", Toast.LENGTH_LONG).show();
                }else {


                    // Retrieve the text entered from the EditText
                    fnametxt = fname.getText().toString();
                    dobtxt = dateView.getText().toString();
                    jobtxt = job.getText().toString();
                    // Force user to fill up the form
                    if (fnametxt.equals("")) {
                        Toast.makeText(getApplicationContext(),
                                "Please complete his name",
                                Toast.LENGTH_LONG).show();
                    } else if (jobtxt.equals("")) {
                        Toast.makeText(getApplicationContext(),
                                "Please complete his job",
                                Toast.LENGTH_LONG).show();
                    } else if (imgFile == null) {
                        Toast.makeText(RegisterActivity2.this, "Please add an image", Toast.LENGTH_SHORT).show();
                    } else {
                        ParseObject father = new ParseObject("Person");
                        father.put("Gender", "Male");
                        father.put("DOB", dobtxt);
                        father.put("LastName", sharedLastName);
                        father.put("FirstName", fnametxt);
                        father.put("Job", job.getText().toString());
                        father.put("image", imgFile);
                        father.put("Email", "unk@unk.com");
                        father.put("Country", sharedCountry);
                        father.put("FatherP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
                        father.put("MotherP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
                        father.put("SpouseP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
                        try {
                            father.save();
                        }catch (ParseException pe){
                            Toast.makeText(RegisterActivity2.this,
                                    "Save Error: " + pe.getMessage(), Toast.LENGTH_LONG)
                                    .show();}
                        finally {

                            sharedInformation.sharedParseFatherId = father.getObjectId();
                            sharedInformation.sharedParseFather = father;
                            //Log.w("father id register", sharedInformation.sharedParseFatherId);
                            Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                    }
                }
            }
        });
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
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        Bitmap bitmap 	= null;
        String path		= "";

        if (requestCode == PICK_FROM_FILE) {
            imageCaptureUri = data.getData();
            path = getRealPathFromURI(imageCaptureUri); //from Gallery

            if (path == null)
                path = imageCaptureUri.getPath(); //from File Manager

            if (path != null)
                bitmap 	= BitmapFactory.decodeFile(path);
        } else {
            path	= imageCaptureUri.getPath();
            bitmap  = BitmapFactory.decodeFile(path);
        }

        mImageView.setImageBitmap(bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //  bitmap.compress(Bitmap.CompressFormat.JPEG,20 , stream);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bitmap, 300, 300, false);
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG,50 , stream);
        // get byte array here
        byte[] bytearray = stream.toByteArray();
        imgFile = new ParseFile ("braveImg.jpg", bytearray);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj 		= {MediaStore.Images.Media.DATA};
        Cursor cursor 		= managedQuery(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index 	= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity2.this,LoginActivity.class);
        startActivity(intent);
    }
}

