package tn.esprit.familylinks.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tn.esprit.familylinks.MainActivity;
import tn.esprit.familylinks.R;

/**
 * Created by L on 27-12-15.
 */
public class familyListAdapter extends RecyclerView.Adapter<familyListAdapter.UserViewHolder> {

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public user currentItem=null;
        public user lastSelectedItem=new user();
        CardView cv;
        TextView personName;
        TextView personInfo;
        //TextView personAge;
        ImageView personPhoto;
        View greenlastItemView=null;
        View whitelastItemView=null;
        //ParseFile imageFile;
        String toast;


        UserViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            view.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View itemView) {
                    Log.w("position long", currentItem.name);
                    toast = "Add a relation to " + currentItem.name;
                    sharedInformation.sharedSelectedPerson=currentItem;
                    Toast.makeText(view.getContext(), toast, Toast.LENGTH_LONG).show();

                    return false;
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View itemView) {
                    sharedInformation.sharedSelectedPerson=currentItem;
                    Log.w("position",currentItem.name);
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra("searched", currentItem.personId);
                    context.startActivity(intent); // item clicked
                    Log.w("position",currentItem.name);
                }
            });

            cv = (CardView)itemView.findViewById(R.id.cvLF);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personInfo = (TextView)itemView.findViewById(R.id.person_info);
            //personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

    List<user> users;
    public familyListAdapter(List<user> users){
        this.users = users;
    }

    @Override
    public int getItemCount() {
        return (null != users ? users.size() : 0);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_membre_famille, viewGroup, false);
        UserViewHolder pvh = new UserViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int i) {
        userViewHolder.personName.setText(users.get(i).name+" "+users.get(i).lastName+" ("+users.get(i).age+")");
        userViewHolder.personInfo.setText(users.get(i).job);
        // userViewHolder.personAge.setText(users.get(i).age);
        userViewHolder.personPhoto.setImageBitmap(users.get(i).photoIS);
        userViewHolder.currentItem = users.get(i);
    }
}