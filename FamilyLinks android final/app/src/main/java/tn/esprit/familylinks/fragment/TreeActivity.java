package tn.esprit.familylinks.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import tn.esprit.familylinks.AddRelation;
import tn.esprit.familylinks.MainActivity;
import tn.esprit.familylinks.ProfileOtherActivity;
import tn.esprit.familylinks.R;
import tn.esprit.familylinks.Utils.FamilyMemberAdapter;
import tn.esprit.familylinks.Utils.RoundImage;
import tn.esprit.familylinks.Utils.sharedInformation;
import tn.esprit.familylinks.Utils.user;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TreeActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TreeActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TreeActivity extends Fragment {
    public List<user> users;
    public List<user> brothers;
    public List<user> childrens;
    private RecyclerView rv;
    private RecyclerView childList;
    private RecyclerView brotherList;
    TextView motherName;
    TextView fatherName;
    TextView spouseName;
    TextView selectedName;
    ParseUser currentUser =ParseUser.getCurrentUser();
    ParseUser user;
    ImageView personPhoto;
    ImageView fatherPhoto;
    ImageView motherPhoto;
    ImageView spousePhoto;
    CardView fatherCardView;
    CardView motherCardView;
    CardView spouseCardView;
    RoundImage roundedImage;
    private Button goProfile;
    private SearchBox search;
    Button btnshare;
    ProgressDialog mProgressDialog;
    private List<user> sharedUsers;

    private OnFragmentInteractionListener mListener;
    public TreeActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TreeActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static TreeActivity newInstance(String param1, String param2) {
        TreeActivity fragment = new TreeActivity();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tree, container, false);

        //************search in list family******************************
        search = (SearchBox) view.findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);
        //searchData();
        for (user j : sharedUsers) {
            SearchResult option = new SearchResult(j.lastName+" "+ j.name, getResources().getDrawable(R.drawable.ic_history),j.personId);
            search.addSearchable(option);
        }

        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
            }

            @Override
            public void onSearchTermChanged(String term) {
                //React to the search term changing
                //Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(getActivity().getApplication(), searchTerm + " Searched", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResultClick(SearchResult result) {
                Intent intent = new Intent(getActivity().getApplication(),MainActivity.class);
                intent.putExtra("searched", result.userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                //React to a result being clicked
            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
            }

        });


        //*************************adapter for tree***************************************

        rv = (RecyclerView) view.findViewById(R.id.rv);
        childList = (RecyclerView) view.findViewById(R.id.childList);
        motherName = (TextView) view.findViewById(R.id.motherLabel);
        fatherName = (TextView) view.findViewById(R.id.fatherLabel);
        spouseName = (TextView) view.findViewById(R.id.spouseLabel);
        selectedName= (TextView) view.findViewById(R.id.selectedPersonLabel);
        personPhoto = (ImageView) view.findViewById(R.id.selectedPerson);
        fatherPhoto = (ImageView) view.findViewById(R.id.addFather);
        motherPhoto = (ImageView) view.findViewById(R.id.addMother);
        spousePhoto = (ImageView) view.findViewById(R.id.addSpouse);
        fatherCardView = (CardView) view.findViewById(R.id.fatherCardView);
        motherCardView = (CardView) view.findViewById(R.id.motherCardView);
        spouseCardView = (CardView) view.findViewById(R.id.spouseCardView);
        goProfile = (Button) view.findViewById(R.id.profileSelected);
        goProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplication() , ProfileOtherActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();
            }
        });
        final View header = inflater.inflate(R.layout.fragment_tree, null);
        btnshare=(Button)view.findViewById(R.id.click);
        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout layout = (RelativeLayout) header.findViewById(R.id.fragmentTree);
                Bitmap bitmap = getScreenShot(layout);
                shareImage(store(bitmap, "img.jpeg"));
            }
        });
        LinearLayoutManager lb = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lb);
        LinearLayoutManager lc = new LinearLayoutManager(getActivity());
        childList.setLayoutManager(lc);
        Log.d("personId", sharedInformation.sharedcurrentPerson.personId);
        // initializeData();
        return view;
    }

    public Bitmap getScreenShot(View view) {
        View v= view.findViewById(R.id.fragmentTree).getRootView();
        v.setDrawingCacheEnabled(true);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false); // clear drawing cache
        return b;
    }

    public File store(Bitmap bm, String fileName){
        final String director = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(director);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("erreur", e.getMessage());
        }
        return  file;
    }

    private void shareImage(File file){
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share Screenshot"));
    }
    public class LoadTree extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Successfully add relation");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading Data");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
            sharedUsers=sharedInformation.sharedUsers;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            users = new ArrayList<>();
            brothers = new ArrayList<>();
            childrens = new ArrayList<>();

                    try {
                        brothers.clear();
                        childrens.clear();
                        for (user j : sharedUsers) {
                            // Log.d("user Last Name ", j.getParseUser("user1"));
                            if(!j.personId.equals("RyQlY5U6UM")){
                                //Log.d("person ", j.toString());
                                if (((j.fatherId.equals(sharedInformation.sharedSelectedPerson.fatherId))&&(!j.fatherId.equals("RyQlY5U6UM")))
                                        ||(j.motherId.equals(sharedInformation.sharedSelectedPerson.motherId))&&(!j.motherId.equals("RyQlY5U6UM"))) {
                                    //Uri imgUri = Uri.parse(U.getParseFile("image").getUrl());
                                    //Log.d("Brother in ", U.getString("LastName"));
                                    if(!j.personId.equals(sharedInformation.sharedSelectedPerson.personId))
                                        brothers.add(j);
                                    //} else if (j.getString("TypeRelation").equals("Mother")) {
                                    // motherName.setText(U.getString("FirstName"));
                                    //} else if (j.getString("TypeRelation").equals("Father")) {
                                    //  fatherName.setText(U.getString("FirstName"));
                                    //} else if (j.getString("TypeRelation").equals("Spouse")) {
                                    //  spouseName.setText(U.getString("FirstName"));
                                } else if ((j.fatherId.equals(sharedInformation.sharedSelectedPerson.personId))
                                        ||(j.motherId.equals(sharedInformation.sharedSelectedPerson.personId))) {
                                        childrens.add(j);
                                }
                            }
                        }
                    } catch (Exception e) {

                    }

            try {
                // Simulate network access.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            getActivity().setTitle(sharedInformation.sharedSelectedPerson.name + " " + sharedInformation.sharedSelectedPerson.lastName);
            //personPhoto.setImageBitmap(sharedInformation.sharedSelectedPerson.photoIS);
            //Bitmap bm = BitmapFactory.(sharedInformation.sharedSelectedPerson.photoIS);
            roundedImage = new RoundImage(sharedInformation.sharedSelectedPerson.photoIS);
            personPhoto.setImageDrawable(roundedImage);

            MainActivity a = (MainActivity) getContext();
            final user f=a.findUser(sharedInformation.sharedSelectedPerson.fatherId);
            if(!f.personId.equals("RyQlY5U6UM")){
                Log.e("u.personId",f.personId);
                fatherPhoto.setImageBitmap(f.photoIS);
                fatherName.setText(f.name + " " + f.lastName);
                fatherCardView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View itemView) {
                        sharedInformation.sharedSelectedPerson = f;
                        Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                        intent.putExtra("searched", f.personId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }else{
                Log.e("u.fatherId", sharedInformation.sharedSelectedPerson.fatherId);
                fatherPhoto.setImageResource(R.drawable.ic_action_add_person);
                fatherName.setText("Add father");
                fatherCardView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View itemView) {
                        Intent intent = new Intent(getActivity().getApplication(), AddRelation.class);
                        intent.putExtra("relation", "Father");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                });
            }

            final user m=a.findUser(sharedInformation.sharedSelectedPerson.motherId);
            if(!m.personId.equals("RyQlY5U6UM")){
                Log.e("m.motherId",m.personId);
                motherPhoto.setImageBitmap(m.photoIS);
                motherName.setText(m.name+" "+m.lastName);
                motherCardView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View itemView) {
                        sharedInformation.sharedSelectedPerson = m;
                        Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                        intent.putExtra("searched", m.personId);
                        startActivity(intent);

                    }
                });
            }else{
                motherPhoto.setImageResource(R.drawable.ic_action_add_person);
                motherName.setText("Add Mother");
                motherCardView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View itemView) {
                        Intent intent = new Intent(getActivity().getApplication(), AddRelation.class);
                        intent.putExtra("relation","Mother");
                        startActivity(intent);
                    }
                });
            }
            final user s=a.findUser(sharedInformation.sharedSelectedPerson.spouseId);
            if(!s.personId.equals("RyQlY5U6UM")){
                //spousePhoto.setImageBitmap(s.photoIS);;
                roundedImage = new RoundImage(s.photoIS);
                spousePhoto.setImageDrawable(roundedImage);
                spouseName.setText(s.name + " " + s.lastName);
                spouseCardView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View itemView) {
                        sharedInformation.sharedSelectedPerson = s;
                        Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                        intent.putExtra("searched", s.personId);
                        startActivity(intent);
                    }
                });
            }else{
                spousePhoto.setImageResource(R.drawable.ic_action_add_person);
                spouseName.setText("Add Spouse");
                spouseCardView.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View itemView) {
                        Intent intent = new Intent(getActivity().getApplication(), AddRelation.class);
                        intent.putExtra("relation", "Spouse");
                        startActivity(intent);
                    }
                });
            }

            FamilyMemberAdapter adapter = new FamilyMemberAdapter(brothers);
            rv.setAdapter(adapter);
            FamilyMemberAdapter adapterChild = new FamilyMemberAdapter(childrens);
            childList.setAdapter(adapterChild);
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LoadTree fami=new LoadTree();
        fami.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
