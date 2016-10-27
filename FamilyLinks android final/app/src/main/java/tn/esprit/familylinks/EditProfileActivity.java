package tn.esprit.familylinks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

import tn.esprit.familylinks.Utils.sharedInformation;
import tn.esprit.familylinks.fragment.ProfileActivity;

public class EditProfileActivity extends Activity {
    EditText name;
    EditText lastname;
    EditText password;
    EditText email;
    Button editB;
    ParseFile imgFileUpdate ;
    private Uri imageCaptureUri;
    private ImageView mImageView;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final String [] items			= new String [] {"From Camera", "From SD Card"};
        ArrayAdapter<String> adapter2	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder		= new AlertDialog.Builder(this);

        builder.setTitle("Select Image");
        builder.setAdapter( adapter2, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) {
                if (item == 0) {
                    Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file		 = new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                    imageCaptureUri = Uri.fromFile(file);

                    try {
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageCaptureUri);
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
                } else {
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);


                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        } );

        final AlertDialog dialog = builder.create();

        mImageView = (ImageView) findViewById(R.id.img_show);

        ((Button) findViewById(R.id.btn_choose_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        name = (EditText)findViewById(R.id.Name);
        lastname = (EditText)findViewById(R.id.LastName);
        password = (EditText)findViewById(R.id.editpass);
        email = (EditText)findViewById(R.id.editemail);
        editB = (Button) findViewById(R.id.editbutton);
        name.setText(sharedInformation.sharedcurrentPerson.name);
        lastname.setText(sharedInformation.sharedcurrentPerson.lastName);
        password.setText(sharedInformation.sharedcurrentPerson.job);
        email.setText(sharedInformation.sharedcurrentPerson.mail);
        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] data = "Working at Parse is great!".getBytes();
                ParseFile file = new ParseFile("drawable/b50.png", data);
                file.saveInBackground();
                ParseObject Person = ParseObject.createWithoutData("Person",sharedInformation.sharedcurrentPerson.personId);



                Person.put("FirstName", name.getText().toString());
                Person.put("LastName", lastname.getText().toString());
                Person.put("Job", password.getText().toString());
                Person.put("Email", email.getText().toString());
                if (imgFileUpdate != null) {  Person.put("image", imgFileUpdate);
                }else Toast.makeText(EditProfileActivity.this, "3abbi nayek l'image", Toast.LENGTH_SHORT).show();/*else{
                    Person.put("FirstName", name.getText().toString());
                    Person.put("LastName", lastname.getText().toString());
                    Person.put("Job", password.getText().toString());
                    Person.put("Email", email.getText().toString());
                    Person.put("image",file);}*/

                Person.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(EditProfileActivity.this, "c BON YA ZEBI ", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        } else {
                            // The save failed.
                        }
                    }


                });
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
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
        imgFileUpdate = new ParseFile ("braveImg.jpg", bytearray);

    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj 		= {MediaStore.Images.Media.DATA};
        Cursor cursor 		= managedQuery( contentUri, proj, null, null,null);

        if (cursor == null) return null;

        int column_index 	= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

}
