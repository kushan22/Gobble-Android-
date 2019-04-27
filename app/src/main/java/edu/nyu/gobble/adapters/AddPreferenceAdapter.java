package edu.nyu.gobble.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nyu.gobble.AddTrips;
import edu.nyu.gobble.R;

public class AddPreferenceAdapter extends RecyclerView.Adapter<AddPreferenceAdapter.AddPreferenceHolder> {

    private Context context;
    private ArrayList<String> likesList;

    public AddPreferenceAdapter(Context context, ArrayList<String> likesList) {

        this.context = context;
        this.likesList = likesList;

    }

    @NonNull
    @Override
    public AddPreferenceAdapter.AddPreferenceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_preferences,viewGroup,false);
        AddPreferenceHolder addPreferenceHolder = new AddPreferenceHolder(v);
        return addPreferenceHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddPreferenceAdapter.AddPreferenceHolder addPreferenceHolder, int i) {
        addPreferenceHolder.tvLikes.setText(likesList.get(i));
    }

    @Override
    public int getItemCount() {
        return likesList.size();
    }

    class AddPreferenceHolder extends RecyclerView.ViewHolder{

        private TextView tvLikes;

        public AddPreferenceHolder(@NonNull View itemView) {
            super(itemView);
            tvLikes = itemView.findViewById(R.id.tvCommonPreferences);
        }
    }
}
