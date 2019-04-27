package edu.nyu.gobble.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nyu.gobble.AddTrips;
import edu.nyu.gobble.R;
import edu.nyu.gobble.pojo.AddFriendModel;
import edu.nyu.gobble.pojo.Model;

public class TripAddFriendsAdapter extends RecyclerView.Adapter<TripAddFriendsAdapter.TripAddFriendsHolder> {

    private Context context;
    private ArrayList<AddFriendModel> friendsData;

    public TripAddFriendsAdapter(Context context, ArrayList<AddFriendModel> friendsData) {

        this.context = context;
        this.friendsData = friendsData;
    }

    @NonNull
    @Override
    public TripAddFriendsAdapter.TripAddFriendsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_common,viewGroup,false);
        TripAddFriendsHolder tripAddFriendsHolder = new TripAddFriendsHolder(v);
        return tripAddFriendsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TripAddFriendsAdapter.TripAddFriendsHolder tripAddFriendsHolder, int i) {
        final AddFriendModel model = friendsData.get(i);
        tripAddFriendsHolder.tvTripAddFriends.setText(model.getFullName());
        tripAddFriendsHolder.view.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);

        tripAddFriendsHolder.tvTripAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setSelected(!model.isSelected());
                tripAddFriendsHolder.view.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsData.size();
    }

    class TripAddFriendsHolder extends RecyclerView.ViewHolder{

        private TextView tvTripAddFriends;
        private View view;

        public TripAddFriendsHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTripAddFriends = itemView.findViewById(R.id.tvCommonText);
            view = itemView;
        }
    }
}
