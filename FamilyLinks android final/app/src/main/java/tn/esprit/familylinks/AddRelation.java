package tn.esprit.familylinks;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import tn.esprit.familylinks.Utils.sharedInformation;
import tn.esprit.familylinks.Utils.user;

/**
 * Created by L on 27-12-15.
 */
public class AddRelation extends AppCompatActivity {
    private Uri imageCaptureUri;
    private ImageView mImageView;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    ParseFile imgFile;
    Bitmap resizedBitmap;
    EditText lname ;
    EditText fname;
    EditText job;
    EditText password;
    Spinner gender;
    TextView dob;
    EditText email;
    Button signup;
    String usernametxt;
    String passwordtxt;
    String gendertxt;
    String dobtxt;
    String lnametxt="aaaa";
    String fnametxt;
    String emailtxt;
    String jobtxt;
    Spinner spinner;
    Toolbar toolbar;
    ActionBar actionBar;
    String newObjetId;
    String typeRealtion;
    ParseObject updatingPerson;
    ParseObject selectedPerson;
    ProgressDialog mProgressDialog;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    ParseObject user1;
    String sharedSelectedPersonId;
    String sharedLastName;
    String sharedSelectedName;
    String sharedSelectedGender;
    String sharedSelectedAge;
    String sharedSelectedJob;
    String sharedSelectedCountry;
    Bitmap sharedSelectedPhotoIS;
    String sharedSelectedFatherId;
    String sharedSelectedMotherId;
    String sharedSelectedSpouseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_relation);

                try {
                    // Simulate network access.
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                try {
                    sharedSelectedPersonId=sharedInformation.sharedSelectedPerson.personId;
                    sharedLastName=sharedInformation.sharedSelectedPerson.lastName;
                    Log.e("sharedLastName",sharedLastName);
                    sharedSelectedName=sharedInformation.sharedSelectedPerson.name;
                    sharedSelectedGender=sharedInformation.sharedSelectedPerson.gender;
                    sharedSelectedAge=sharedInformation.sharedSelectedPerson.age;
                    sharedSelectedJob=sharedInformation.sharedSelectedPerson.job;
                    sharedSelectedCountry=sharedInformation.sharedSelectedPerson.country;
                    sharedSelectedPhotoIS=sharedInformation.sharedSelectedPerson.photoIS;
                    sharedSelectedFatherId=sharedInformation.sharedSelectedPerson.fatherId;
                    sharedSelectedMotherId=sharedInformation.sharedSelectedPerson.motherId;
                    sharedSelectedSpouseId=sharedInformation.sharedSelectedPerson.spouseId;
                    selectedPerson=ParseObject.createWithoutData("Person", sharedSelectedPersonId);
                } catch (Exception e) {
                    Log.e("Error", "can't get shared selected");
                }


        final String[] items = new String[]{"From SD Card"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);

        mImageView = (ImageView) findViewById(R.id.img_show);
        ((Button) findViewById(R.id.btn_choose_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                            /*Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);*/

                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, PICK_FROM_FILE);

            }
        });



        Bundle extra=getIntent().getExtras();
        if(extra!=null)
            typeRealtion=extra.getString("relation");
        Log.e("rel",typeRealtion);

        //date picker
        dateView = (TextView) findViewById(R.id.dob);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
//spinner gender
        spinner = (Spinner) findViewById(R.id.gender_spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

// Locate EditTexts in main.xml
        fname = (EditText) findViewById(R.id.FirstName);
        lname = (EditText) findViewById(R.id.LastName);
        job = (EditText) findViewById(R.id.Job);
        //email= (EditText) findViewById(R.id.Email);
        signup = (Button)findViewById(R.id.add_relation_button);
        //Log.e("selected last name",sharedInformation.sharedSelectedPerson.lastName);
        if((!typeRealtion.equals("Spouse")) && (!typeRealtion.equals("Mother"))){ lname.setVisibility(View.GONE); }
        if((typeRealtion.equals("Father")) || (typeRealtion.equals("Mother"))){ spinner.setVisibility(View.GONE); }

        /*actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
*/
        signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernametxt = lname.getText().toString()+fname.getText().toString();
                //emailtxt=email.getText().toString();
                if((typeRealtion.equals("Spouse")) || (typeRealtion.equals("Mother"))) lnametxt = lname.getText().toString();
                fnametxt = fname.getText().toString();
                jobtxt = job.getText().toString();
                if(typeRealtion.equals("Father"))gendertxt="Male";
                if(typeRealtion.equals("Mother"))gendertxt="Female";
                else gendertxt =spinner.getSelectedItem().toString();
                dobtxt =dateView.getText().toString();
                // Force user to fill up the form
                if (fname.equals("") ) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();
                }if (lname.equals("") ) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();
                }if (jobtxt.equals("") ) {
                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (imgFile != null) {
                        // Save new user data into Parse.com Data Storage
                        RegisterTask registerTask = new RegisterTask();
                        registerTask.execute();

                    }
                    else Toast.makeText(AddRelation.this, "Add image please ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public class RegisterTask extends AsyncTask<Void, Integer, Void> {
        String addControl="ok";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(AddRelation.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Successfully add relation");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading Data");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
            if((!typeRealtion.equals("Spouse")) && (!typeRealtion.equals("Mother"))){ lnametxt=sharedLastName; }
        }

        @Override
        protected Void doInBackground(Void... arg0) {

                user1 = new ParseObject("Person");
                user1.put("FirstName", fnametxt);
                user1.put("LastName", lnametxt);
                user1.put("Gender", gendertxt);
                user1.put("DOB", dobtxt);
                user1.put("Job", jobtxt);
                user1.put("Country",  sharedSelectedCountry);
                user1.put("Email", "unk@unk.com");
                user1.put("image", imgFile);
                user1.put("FatherP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
                user1.put("MotherP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
                user1.put("SpouseP", ParseObject.createWithoutData("Person", "RyQlY5U6UM"));
                if (typeRealtion.equals("Children")) {
                    Log.w("sharedSelectedSpouseId", sharedSelectedSpouseId);
                    Log.w("selectedPersonID", selectedPerson.getObjectId());
                    if (sharedSelectedGender.equals("Male")) {
                        user1.put("FatherP", selectedPerson);
                        user1.put("MotherP", ParseObject.createWithoutData("Person", sharedSelectedSpouseId));
                    } else {
                        user1.put("MotherP", selectedPerson);
                        user1.put("FatherP", ParseObject.createWithoutData("Person", sharedSelectedSpouseId));
                    }
                }
                else if (typeRealtion.equals("Brother")) {
                    Log.w("sharedSelectedFatherId", sharedSelectedFatherId);
                    user1.put("FatherP", ParseObject.createWithoutData("Person", sharedSelectedFatherId));
                    user1.put("MotherP", ParseObject.createWithoutData("Person", sharedSelectedMotherId));
                }
                else if (typeRealtion.equals("Mother"))
                    user1.put("SpouseP", ParseObject.createWithoutData("Person",sharedSelectedFatherId));
                else if (typeRealtion.equals("Father"))
                    user1.put("SpouseP", ParseObject.createWithoutData("Person",sharedSelectedMotherId));
                else if (typeRealtion.equals("Spouse"))
                    user1.put("SpouseP", ParseObject.createWithoutData("Person",sharedSelectedPersonId));

            try {
                user1.save();
            }catch (ParseException pe){
                Log.e("Errorrr1", pe.getMessage());
                addControl="no";
            }


            Log.w("updatingw personId", sharedSelectedPersonId);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Person");
            query.include("MotherP");
            // Retrieve the object by id
            query.getInBackground(sharedSelectedPersonId, new GetCallback<ParseObject>() {

                public void done(ParseObject per, ParseException e) {

                    if (e == null) {
                        Log.w("PersonName", user1.getString("LastName"));

                        if (typeRealtion.equals("Spouse")) {
                            per.put("SpouseP", user1);
                            try{
                                per.save();
                            }catch (ParseException pe){
                                Log.e("Errorrr2", pe.getMessage());
                                addControl="no";
                            }
                            user1.put("SpouseP", selectedPerson);
                            try {
                                user1.save();
                            }catch (ParseException pe){
                                Log.e("Errorrr3", pe.getMessage());
                                addControl="no";
                            }

                            /*user u = new user(sharedSelectedPersonId,
                                    sharedSelectedName,
                                    sharedLastName,
                                    sharedSelectedGender,
                                    sharedSelectedAge,
                                    sharedSelectedJob,
                                    sharedSelectedCountry,
                                    sharedSelectedPhotoIS,
                                    sharedSelectedFatherId,
                                    sharedSelectedMotherId,
                                    user1.getObjectId());
                            Log.e("size before delete", Integer.toString(sharedInformation.sharedUsers.size()));
                            sharedInformation.sharedUsers.remove(sharedInformation.findUser(sharedSelectedPersonId));
                            Log.e("size after delete", Integer.toString(sharedInformation.sharedUsers.size()));
                            sharedInformation.sharedUsers.add(u);
                            Log.e("size before add", Integer.toString(sharedInformation.sharedUsers.size()));*/
                            //Log.w("SpouseOK", user.getString("LastName"));'


                        }


                        /*else if (typeRealtion.equals("Parent")) {
                            if (gendertxt.equals("Male")) {
                                per.put("FatherP", user1);
                                per.put("MotherP", ParseObject.createWithoutData("Person", sharedSelectedMotherId));
                            } else {
                                per.put("MotherP", user1);
                                per.put("FatherP", ParseObject.createWithoutData("Person", sharedSelectedFatherId));
                            }
                            try {
                                per.save();
                            }catch (ParseException pe){
                                addControl="no";
                                Toast.makeText(AddRelation.this,
                                        "Save Error: " + pe.getMessage(), Toast.LENGTH_LONG)
                                        .show();}
                        }*/


                        else if (typeRealtion.equals("Father")) {
                            per.put("FatherP", user1);
                            try {
                                per.save();
                            }catch (ParseException pe){
                                Log.e("Errorrr4", pe.getMessage());
                                addControl="no";
                            }
                        } else if (typeRealtion.equals("Mother")) {
                            per.put("MotherP", user1);
                            //per.put("FatherP", ParseObject.createWithoutData("Person", sharedSelectedFatherId));
                            try {
                                per.save();
                            }catch (ParseException pe){
                                Log.e("Errorrr5", pe.getMessage());
                                addControl="no";
                            }
                        }

                        Log.w("idPerson", sharedSelectedPersonId);
                        user newU = new user(user1.getObjectId(),
                                fnametxt,
                                lnametxt,
                                gendertxt,
                                dobtxt,
                                jobtxt,
                                sharedSelectedCountry,
                                resizedBitmap,
                                user1.getParseObject("FatherP").getObjectId(),
                                user1.getParseObject("MotherP").getObjectId(),
                                user1.getParseObject("SpouseP").getObjectId());

                        try {
                            sharedInformation.sharedUsers.add(newU);

                        } catch (Exception exp) {
                            Log.e("Errorrr6", exp.getMessage());
                            e.printStackTrace();
                            addControl="no";
                        }

                        //*********update list users************
                        if((typeRealtion.equals("Spouse"))||(typeRealtion.equals("Father"))||(typeRealtion.equals("Mother")))
                        {
                            sharedInformation.sharedUsers.remove(sharedInformation.sharedSelectedPerson);
                            user newSelected=sharedInformation.sharedSelectedPerson;
                            if(typeRealtion.equals("Spouse"))newSelected.spouseId=user1.getObjectId();
                            if(typeRealtion.equals("Father"))newSelected.fatherId=user1.getObjectId();
                            if(typeRealtion.equals("Mother"))newSelected.motherId=user1.getObjectId();
                            sharedInformation.sharedUsers.add(newSelected);
                        }



                        /*Intent intent = new Intent(getApplication(), MainActivity.class);
                        intent.putExtra("searched", sharedInformation.sharedSelectedPerson.personId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();*/
                    } else {
                        Toast.makeText(getApplicationContext(),
                                e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            try {
                // Simulate network access.
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(addControl.equals("ok")){
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.putExtra("searched", sharedInformation.sharedSelectedPerson.personId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                mProgressDialog.dismiss();
                finish();} else {
                Toast.makeText(AddRelation.this,
                        "Save Error", Toast.LENGTH_LONG)
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
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        if (resultCode != RESULT_OK || data.getData() == null) return;
            Bitmap bitmap = null;
            String path = "";

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver()
                    .query(selectedImage, filePathColumn, null, null,
                            null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            bitmap = (BitmapFactory.decodeFile(picturePath));

            mImageView.setImageBitmap(bitmap);
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //  bitmap.compress(Bitmap.CompressFormat.JPEG,20 , stream);
                resizedBitmap = Bitmap.createScaledBitmap(
                        bitmap, 400, 400, false);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                // get byte array here
                byte[] bytearray = stream.toByteArray();
                imgFile = new ParseFile("AddRelAndroid.jpg", bytearray);
            }catch (Exception pe){
                Toast.makeText(AddRelation.this,
                        "Error loading image", Toast.LENGTH_LONG)
                        .show();
            }

                    /*if (requestCode == PICK_FROM_FILE) {
                        Uri uri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                            mImageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    /*    imageCaptureUri = data.getData();
                        path = getRealPathFromURI(imageCaptureUri); //from Gallery

                        if (path == null)
                            path = imageCaptureUri.getPath(); //from File Manager

                        if (path != null)
                            bitmap = BitmapFactory.decodeFile(path);
                    } else {
                        path = imageCaptureUri.getPath();
                        bitmap = BitmapFactory.decodeFile(path);
                    }*/




                    /*try {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        //  bitmap.compress(Bitmap.CompressFormat.JPEG,20 , stream);
                        resizedBitmap = Bitmap.createScaledBitmap(
                                bitmap, 400, 400, false);
                        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        // get byte array here
                        byte[] bytearray = stream.toByteArray();
                        imgFile = new ParseFile("AddRelAndroid.jpg", bytearray);
                    }catch (Exception pe){
                        Toast.makeText(AddRelation.this,
                                "Error loading image", Toast.LENGTH_LONG)
                                .show();}*/






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
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.putExtra("searched", sharedInformation.sharedSelectedPerson.personId);
        startActivity(intent);
    }

}