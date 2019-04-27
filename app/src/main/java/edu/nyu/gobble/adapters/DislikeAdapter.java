package edu.nyu.gobble.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


import edu.nyu.gobble.R;
import edu.nyu.gobble.pojo.Model;

public class DislikeAdapter extends RecyclerView.Adapter<DislikeAdapter.DislikeHolder> {

    private Context context;
    private ArrayList<Model> dislikesData;

    public DislikeAdapter(Context context, ArrayList<Model> dislikesData) {
        this.context = context;
        this.dislikesData = dislikesData;
    }

    @NonNull
    @Override
    public DislikeAdapter.DislikeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_common,viewGroup,false);
        DislikeHolder dislikeHolder = new DislikeHolder(view);

        return dislikeHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DislikeAdapter.DislikeHolder dislikeHolder, int i) {

        final Model model = dislikesData.get(i);
        dislikeHolder.tvDislikes.setText(model.getText());
        dislikeHolder.view.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);

        dislikeHolder.tvDislikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setSelected(!model.isSelected());
                dislikeHolder.view.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dislikesData.size();
    }

    class DislikeHolder extends RecyclerView.ViewHolder{

        private TextView tvDislikes;
        private View view;
        public DislikeHolder(@NonNull View itemView) {
            super(itemView);

            tvDislikes = itemView.findViewById(R.id.tvCommonText);
            view = itemView;
        }
    }
}
