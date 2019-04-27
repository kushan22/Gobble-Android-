package edu.nyu.gobble.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nyu.gobble.AddTrips;
import edu.nyu.gobble.R;

public class AddDislikeAdapter extends RecyclerView.Adapter<AddDislikeAdapter.AddDislikeHolder> {

    private Context context;
    private ArrayList<String> dislikesList;

    public AddDislikeAdapter(Context context, ArrayList<String> dislikesList) {
        this.context = context;
        this.dislikesList = dislikesList;
    }

    @NonNull
    @Override
    public AddDislikeAdapter.AddDislikeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_preferences,viewGroup,false);
        AddDislikeHolder addDislikeHolder = new AddDislikeHolder(v);
        return addDislikeHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddDislikeAdapter.AddDislikeHolder addDislikeHolder, int i) {
        addDislikeHolder.tvDislikes.setText(dislikesList.get(i));
    }

    @Override
    public int getItemCount() {
        return dislikesList.size();
    }

    class AddDislikeHolder extends RecyclerView.ViewHolder{

        private TextView tvDislikes;
        public AddDislikeHolder(@NonNull View itemView) {
            super(itemView);
            tvDislikes = itemView.findViewById(R.id.tvCommonPreferences);
        }
    }
}
