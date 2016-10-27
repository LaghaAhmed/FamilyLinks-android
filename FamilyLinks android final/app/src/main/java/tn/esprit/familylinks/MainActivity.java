package tn.esprit.familylinks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseUser;

import tn.esprit.familylinks.Utils.sharedInformation;
import tn.esprit.familylinks.Utils.user;
import tn.esprit.familylinks.fragment.HomeActivity;
import tn.esprit.familylinks.fragment.ListActivity;
import tn.esprit.familylinks.fragment.ProfileActivity;
import tn.esprit.familylinks.fragment.TreeActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LinearLayout Header;
    TextView nameHeader;
    TextView emailheader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentMain, new HomeActivity()).addToBackStack(null).commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        Header = (LinearLayout) header.findViewById(R.id.header);
        nameHeader= (TextView) header.findViewById(R.id.nameHeader);
        emailheader= (TextView) header.findViewById(R.id.emailHeader);

        Bitmap bitmap = sharedInformation.sharedcurrentPerson.photoIS;
        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
        Header.setBackgroundDrawable(ob);
        Log.e("sharedddd", sharedInformation.sharedcurrentPerson.name);
        nameHeader.setText(sharedInformation.sharedcurrentPerson.name + " " + sharedInformation.sharedcurrentPerson.lastName);
        emailheader.setText(sharedInformation.sharedcurrentPerson.mail);


        // Recevoir les donn�es
        Bundle extras = getIntent().getExtras();
        if (extras == null) {

        }else{
            // R�cup�rer le choix
            String value1 = extras.getString("searched");
            if (value1 != null) {
                //getSupportFragmentManager().beginTransaction().detach(TreeFragment).commit();
                sharedInformation.sharedSelectedPerson=findUser(value1);
                Log.e("sharedSelectedMain", sharedInformation.sharedSelectedPerson.personId + " " +sharedInformation.sharedSelectedPerson.name + " " + sharedInformation.sharedSelectedPerson.lastName);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new TreeActivity()).addToBackStack(null).commit();

               // Toast.makeText(getApplicationContext(),value1,Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_navigation_drawer_home) {
            getSupportFragmentManager().beginTransaction()
                   .replace(R.id.fragmentMain, new HomeActivity()).addToBackStack(null).commit();
        } else if (id == R.id.item_navigation_drawer_profile) {
            sharedInformation.sharedSelectedPerson=sharedInformation.sharedcurrentPerson;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentMain, new ProfileActivity()).addToBackStack(null).commit();

        }else if (id == R.id.item_navigation_drawer_listed1) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentMain, new ListActivity()).addToBackStack(null).commit();
        } else if (id == R.id.item_navigation_drawer_tree) {
            sharedInformation.sharedSelectedPerson=sharedInformation.sharedcurrentPerson;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentMain, new TreeActivity()).addToBackStack(null).commit();

        }  else if (id == R.id.item_navigation_drawer_logout) {

            ParseUser.logOut();
            finish();
            Intent intent3 = new Intent(MainActivity.this, LoginActivity.class);
            intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent3);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addChild(View view) {
        Intent data =new Intent(MainActivity.this, AddRelation.class);
        data.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        data.putExtra("relation", "Children");
        startActivity(data);
    }

    public void addMother(View view) {
        Intent data =new Intent(MainActivity.this, AddRelation.class);
        data.putExtra("relation","Mother");
        startActivity(data);
    }
    public user findUser(String objId)
    {
        for(user u:sharedInformation.sharedUsers){
            if(u.personId.equals(objId))return u;
        }
        user u=new user(objId);
        return u;
    }

}
