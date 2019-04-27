package edu.nyu.gobble.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nyu.gobble.R;
import edu.nyu.gobble.SetProfile;
import edu.nyu.gobble.pojo.Model;

public class ConsiderationsAdapter extends RecyclerView.Adapter<ConsiderationsAdapter.ConsiderationHolder> {

    private Context context;
    private ArrayList<Model> considerationData;

    public ConsiderationsAdapter(Context context, ArrayList<Model> considerationData) {
        this.context = context;
        this.considerationData = considerationData;
    }

    @NonNull
    @Override
    public ConsiderationsAdapter.ConsiderationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_common,viewGroup,false);
        ConsiderationHolder considerationHolder = new ConsiderationHolder(view);

        return considerationHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ConsiderationsAdapter.ConsiderationHolder considerationHolder, int i) {

        final Model model = considerationData.get(i);
        considerationHolder.tvConsideration.setText(model.getText());
        considerationHolder.view.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);

        considerationHolder.tvConsideration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setSelected(!model.isSelected());
                considerationHolder.view.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return considerationData.size();
    }

    class ConsiderationHolder extends RecyclerView.ViewHolder{

        private TextView tvConsideration;
        private View view;

        public ConsiderationHolder(@NonNull View itemView) {
            super(itemView);
            tvConsideration = itemView.findViewById(R.id.tvCommonText);
            view = itemView;
        }
    }
}
