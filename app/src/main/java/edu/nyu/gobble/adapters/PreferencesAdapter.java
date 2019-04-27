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
import java.util.List;

import edu.nyu.gobble.R;
import edu.nyu.gobble.SetProfile;
import edu.nyu.gobble.pojo.Model;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.PreferencesHolder> {

    private Context context;
    private ArrayList<Model> preferences;

    public PreferencesAdapter(Context context, ArrayList<Model> preferences) {
        this.context = context;
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public PreferencesAdapter.PreferencesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_common,viewGroup,false);
        PreferencesHolder preferencesHolder = new PreferencesHolder(v);
        return preferencesHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PreferencesAdapter.PreferencesHolder preferencesHolder, int i) {
       final Model model = preferences.get(i);
       preferencesHolder.tvPreferences.setText(model.getText());
       preferencesHolder.view.setBackgroundColor(model.isSelected() ? Color.RED : Color.WHITE);

        preferencesHolder.tvPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setSelected(!model.isSelected());
                preferencesHolder.view.setBackgroundColor(model.isSelected() ? Color.RED : Color.WHITE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return preferences.size();
    }

    public class PreferencesHolder extends RecyclerView.ViewHolder{

        private TextView tvPreferences;
        private View view;
        public PreferencesHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvPreferences = itemView.findViewById(R.id.tvCommonText);
        }
    }
}
