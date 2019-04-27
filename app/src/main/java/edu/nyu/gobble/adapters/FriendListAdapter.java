package edu.nyu.gobble.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nyu.gobble.R;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendListHolder> {

    private Context context;
    private ArrayList<String> fullNames;
    public FriendListAdapter(Context context, ArrayList<String> fullNames) {
        this.context = context;
        this.fullNames = fullNames;
    }

    @NonNull
    @Override
    public FriendListAdapter.FriendListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_friendlist,viewGroup,false);
        FriendListHolder friendListHolder = new FriendListHolder(v);
        return friendListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListAdapter.FriendListHolder friendListHolder, int i) {

        friendListHolder.tvFriendName.setText(fullNames.get(i));

    }

    @Override
    public int getItemCount() {
        return fullNames.size();
    }

    class FriendListHolder extends RecyclerView.ViewHolder{

        private TextView tvFriendName;

        public FriendListHolder(@NonNull View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);

        }
    }
}
