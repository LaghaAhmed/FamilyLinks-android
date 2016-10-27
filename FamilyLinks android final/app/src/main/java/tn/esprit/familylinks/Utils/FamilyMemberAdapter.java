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

import java.util.List;

import tn.esprit.familylinks.MainActivity;
import tn.esprit.familylinks.R;

/**
 * Created by L on 28-12-15.
 */
public class FamilyMemberAdapter extends RecyclerView.Adapter<FamilyMemberAdapter.UserViewHolder> {
    public Context context;


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public user currentItem=null;
        CardView cv;
        TextView personName;
        TextView selectedName;
        //TextView personAge;
        ImageView personPhoto;
        //ParseFile imageFile;



        UserViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            //personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View itemView) {
                    // item clicked
                    sharedInformation.sharedSelectedPerson=currentItem;
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context,MainActivity.class);
                    intent.putExtra("searched", currentItem.personId);
                    context.startActivity(intent);
                    Log.w("position", currentItem.name);
                }
            });
        }

    }

    List<user> users;

    public FamilyMemberAdapter(List<user> users){
        this.users = users;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bro_tree, viewGroup, false);
        UserViewHolder pvh = new UserViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int i) {
        if(getItemCount()>0) {
            userViewHolder.personName.setText(users.get(i).name);
            // userViewHolder.personAge.setText(users.get(i).age);
//        Log.d("img in ", bitmap.toString());
            //mImageView.setImageBitmap(bitmap);
            userViewHolder.personPhoto.setImageBitmap(users.get(i).photoIS);//.setImageResource(users.get(i).photoId);
            userViewHolder.currentItem = users.get(i);
        }
    }

    @Override
    public int getItemCount() {
        return (null != users ? users.size() : 0);
    }

}
