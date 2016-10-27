package tn.esprit.familylinks.fragment;

/**
 * Created by L on 15-11-15.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import tn.esprit.familylinks.R;


/**
 * Created by L on 15-11-15.
 */
public class HomeActivity extends Fragment {

    private ImageView a;
    private ImageView logo;
    private ImageView c;
    private ImageView d;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        a=(ImageView) view.findViewById(R.id.a);
        c=(ImageView) view.findViewById(R.id.c);
        logo = (ImageView) view.findViewById(R.id.logoH);
        d=(ImageView) view.findViewById(R.id.d);
        //a.setImageResource(R.drawable.a);
        //c.setImageResource(R.drawable.c);
        //d.setImageResource(R.drawable.d);
        //logo.setImageResource(R.drawable.icone_white);
        return view;
    }


}
