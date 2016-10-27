package tn.esprit.familylinks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import tn.esprit.familylinks.Utils.sharedInformation;
import tn.esprit.familylinks.Utils.user;

public class ProfileOtherActivity extends AppCompatActivity {
    private ImageView mImageView;
    private TextView nameU;
    private TextView job;
    private TextView date;
    private TextView email;
    private Button editprofile;
    private TextView country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_other);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user u= sharedInformation.sharedSelectedPerson;
        //   mListView = (AnimationProfilePhoto) view.findViewById(R.id.layout_listview);
        mImageView = (ImageView) findViewById(R.id.imageUser);
        nameU = (TextView) findViewById(R.id.nameUser);
        job = (TextView) findViewById(R.id.job);
        date = (TextView) findViewById(R.id.dob);
        // email = (TextView) findViewById(R.id.email1);
        country = (TextView) findViewById(R.id.Country);
        Bitmap bitmap = u.photoIS;
        mImageView.setImageBitmap(bitmap);
        String fn = u.name;
        String ln = u.lastName;
        this.setTitle(fn + " " + ln);
        job.setText(u.job);
        date.setText(u.age);
        country.setText(u.country);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplication(), MainActivity.class);

        intent.putExtra("searched", sharedInformation.sharedSelectedPerson.personId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
