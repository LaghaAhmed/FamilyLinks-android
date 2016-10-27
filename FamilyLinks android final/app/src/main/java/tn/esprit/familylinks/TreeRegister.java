package tn.esprit.familylinks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.familylinks.Utils.FamilyMemberAdapter;
import tn.esprit.familylinks.Utils.RoundImage;
import tn.esprit.familylinks.Utils.sharedInformation;
import tn.esprit.familylinks.Utils.user;

public class TreeRegister extends AppCompatActivity {


    public List<tn.esprit.familylinks.Utils.user> users;
    public List<user> brothers;
    public List<user> childrens;
    private RecyclerView rv;
    private RecyclerView childList;
    private RecyclerView brotherList;
    TextView motherName;
    TextView fatherName;
    TextView spouseName;
    TextView selectedName;
    ImageView personPhoto;
    ImageView fatherPhoto;
    ImageView motherPhoto;
    ImageView spousePhoto;
    CardView fatherCardView;
    CardView motherCardView;
    CardView spouseCardView;
    RoundImage roundedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //*************************adapter for tree***************************************

        rv = (RecyclerView) findViewById(R.id.rv);
        childList = (RecyclerView) findViewById(R.id.childList);
        motherName = (TextView) findViewById(R.id.motherLabel);
        fatherName = (TextView) findViewById(R.id.fatherLabel);
        spouseName = (TextView) findViewById(R.id.spouseLabel);
        selectedName= (TextView) findViewById(R.id.selectedPersonLabel);
        personPhoto = (ImageView) findViewById(R.id.selectedPerson);
        fatherPhoto = (ImageView) findViewById(R.id.addFather);
        motherPhoto = (ImageView) findViewById(R.id.addMother);
        spousePhoto = (ImageView) findViewById(R.id.addSpouse);
        fatherCardView = (CardView) findViewById(R.id.fatherCardView);
        motherCardView = (CardView) findViewById(R.id.motherCardView);
        spouseCardView = (CardView) findViewById(R.id.spouseCardView);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        LinearLayoutManager llm2 = new LinearLayoutManager(getApplicationContext());
        childList.setLayoutManager(llm2);
        Log.d("personId", sharedInformation.sharedcurrentPerson.personId);
        // initializeData();
        LoadTree fami=new LoadTree();
        fami.execute();

    }

    public class LoadTree extends AsyncTask<Void, Integer, Void>
    {

        int count = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            users = new ArrayList<>();
            brothers = new ArrayList<>();
            childrens = new ArrayList<>();
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        for (user j : sharedInformation.sharedUsers) {
                            // Log.d("user Last Name ", j.getParseUser("user1"));

                            if ((j.fatherId.equals(sharedInformation.sharedSelectedPerson.fatherId))||(j.motherId.equals(sharedInformation.sharedSelectedPerson.motherId))) {
                                if(!j.personId.equals(sharedInformation.sharedSelectedPerson.personId))
                                    brothers.add(j);
                            } else if ((j.fatherId.equals(sharedInformation.sharedSelectedPerson.personId))||(j.motherId.equals(sharedInformation.sharedSelectedPerson.personId))) {
                                childrens.add(j);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }

                }
            };
            timerThread.start();
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            selectedName.setText(sharedInformation.sharedSelectedPerson.name+" "+sharedInformation.sharedSelectedPerson.lastName);
            roundedImage = new RoundImage(sharedInformation.sharedSelectedPerson.photoIS);
            personPhoto.setImageDrawable(roundedImage);


            final user f=sharedInformation.findUser(sharedInformation.sharedParseFatherId);
            if(!f.personId.equals("RyQlY5U6UM")){
                fatherPhoto.setImageBitmap(f.photoIS);
                fatherName.setText(f.name + " " + f.lastName);
            }

            final user m=sharedInformation.findUser(sharedInformation.sharedParseMotherId);
            if(!m.personId.equals("RyQlY5U6UM")){
                Log.e("m.motherId",m.personId);
                motherPhoto.setImageBitmap(m.photoIS);
                motherName.setText(m.name+" "+m.lastName);
            }

            FamilyMemberAdapter adapter = new FamilyMemberAdapter(brothers);
            rv.setAdapter(adapter);
            FamilyMemberAdapter adapterChild = new FamilyMemberAdapter(childrens);
            childList.setAdapter(adapterChild);
        }
    }

}
