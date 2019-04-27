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

public class AddConsiderationAdapter extends RecyclerView.Adapter<AddConsiderationAdapter.AddConsiderationHolder> {

    private Context context;
    private ArrayList<String> considerationList;

    public AddConsiderationAdapter(Context context, ArrayList<String> considerationList) {
        this.context = context;
        this.considerationList = considerationList;
    }

    @NonNull
    @Override
    public AddConsiderationAdapter.AddConsiderationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_preferences,viewGroup,false);
        AddConsiderationHolder addConsiderationHolder = new AddConsiderationHolder(v);
        return addConsiderationHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddConsiderationAdapter.AddConsiderationHolder addConsiderationHolder, int i) {
        addConsiderationHolder.tvConsideration.setText(considerationList.get(i));
    }

    @Override
    public int getItemCount() {
        return considerationList.size();
    }

    class AddConsiderationHolder extends RecyclerView.ViewHolder{

        private TextView tvConsideration;
        public AddConsiderationHolder(@NonNull View itemView) {
            super(itemView);
            tvConsideration = itemView.findViewById(R.id.tvCommonPreferences);
        }
    }
}
