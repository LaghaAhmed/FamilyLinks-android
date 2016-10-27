package tn.esprit.familylinks.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.familylinks.AddRelation;
import tn.esprit.familylinks.R;
import tn.esprit.familylinks.Utils.familyListAdapter;
import tn.esprit.familylinks.Utils.sharedInformation;
import tn.esprit.familylinks.Utils.user;

/**
 * Created by L on 27-12-15.
 */
public class ListActivity extends Fragment {
    private List<user> users;
    private List<user> usersName;
    private RecyclerView rv;
    private SearchBox search;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        //*************************List***************************************
        rv = (RecyclerView) view.findViewById(R.id.rv2);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData2();
        return view;
    }

    public void initializeData2() {
        users = new ArrayList<>();
        Log.d("initializeData2 ", sharedInformation.sharedUsers.size() + " users");
        initializeAdapter2();

    }

    public void initializeAdapter2() {
        Log.d("initializeAdapter2 ", sharedInformation.sharedUsers.size() + " users");
        familyListAdapter adapter = new familyListAdapter(sharedInformation.sharedUsers);
        rv.setAdapter(adapter);
    }
}