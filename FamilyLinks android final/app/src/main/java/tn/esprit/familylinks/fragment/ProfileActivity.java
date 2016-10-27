package tn.esprit.familylinks.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import tn.esprit.familylinks.AddRelation;
import tn.esprit.familylinks.EditProfileActivity;
import tn.esprit.familylinks.R;
import tn.esprit.familylinks.Utils.sharedInformation;
import tn.esprit.familylinks.Utils.user;

//import tn.esprit.familylinks.Utils.AnimationProfilePhoto;

/**
 * Created by L on 15-11-15.
 */
public class ProfileActivity extends Fragment {
 //   private AnimationProfilePhoto mListView;
    private ImageView mImageView;
    private TextView nameU;
    private TextView pseudo;
    private TextView date;
    private TextView email;
    private Button editprofile;
    private TextView email1;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }

    //@TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_profile, container, false);
        user u=sharedInformation.sharedcurrentPerson;
        sharedInformation.sharedSelectedPerson=u;
        //   mListView = (AnimationProfilePhoto) view.findViewById(R.id.layout_listview);
        View header = inflater.inflate(R.layout.fragment_list_profile, null);
        mImageView = (ImageView) view.findViewById(R.id.imageUser);
        nameU = (TextView) view.findViewById(R.id.nameUser);
        pseudo = (TextView) view.findViewById(R.id.job);
        date = (TextView) view.findViewById(R.id.dob);
        // email = (TextView) view.findViewById(R.id.email1);
        email1 = (TextView) view.findViewById(R.id.email_textview);
        Bitmap bitmap = sharedInformation.sharedcurrentPerson.photoIS;
        mImageView.setImageBitmap(bitmap);
        editprofile = (Button) view.findViewById(R.id.editprof);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplication() , EditProfileActivity.class);
                startActivity(i);
            }
        });


        String fn = sharedInformation.sharedcurrentPerson.name;
        String ln = sharedInformation.sharedcurrentPerson.lastName;
        nameU.setText(fn + " "+ln+"    ");
        pseudo.setText(sharedInformation.sharedcurrentPerson.job);
        date.setText(sharedInformation.sharedcurrentPerson.age);
        email1.setText(sharedInformation.sharedcurrentPerson.mail);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,
                new String[]{

                }
        );
        //  mListView.setAdapter(adapter);

        //floating button
        final View actionSpouse = view.findViewById(R.id.action_Spouse);
        final View actionChildren = view.findViewById(R.id.action_Children);
        final View actionBrother = view.findViewById(R.id.action_Brother);


        actionSpouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data =new Intent(getActivity().getApplication(), AddRelation.class);
                data.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                data.putExtra("relation", "Spouse");
                startActivity(data);
            }
        });

        actionChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data =new Intent(getActivity().getApplication(), AddRelation.class);
                data.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                data.putExtra("relation","Children");
                startActivity(data);
            }
        });
        actionBrother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data =new Intent(getActivity().getApplication(), AddRelation.class);
                data.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                data.putExtra("relation", "Brother");
                startActivity(data);
            }
        });

        return view;
    }

}
