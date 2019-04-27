package edu.nyu.gobble.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nyu.gobble.R;
import edu.nyu.gobble.Recommendation;
import edu.nyu.gobble.pojo.RecommendationModel;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationHolder> {

    private Context context;
    private ArrayList<RecommendationModel> recommendationModels;

    public RecommendationAdapter(Context context, ArrayList<RecommendationModel> recommendationModels) {
        this.context = context;
        this.recommendationModels = recommendationModels;
    }

    @NonNull
    @Override
    public RecommendationAdapter.RecommendationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_recommendation,viewGroup,false);
        RecommendationHolder recommendationHolder = new RecommendationHolder(v);
        return recommendationHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationAdapter.RecommendationHolder recommendationHolder, int i) {
        RecommendationModel recommendationModel = recommendationModels.get(i);

        recommendationHolder.ivRecommendation.setImageResource(recommendationModel.getImageName());
        recommendationHolder.tvRecommendationName.setText(recommendationModel.getName());
        recommendationHolder.tvRecommendationAddress.setText(recommendationModel.getAddress());
    }

    @Override
    public int getItemCount() {
        return recommendationModels.size();
    }

    class RecommendationHolder extends RecyclerView.ViewHolder{

        private ImageView ivRecommendation;
        private TextView tvRecommendationName,tvRecommendationAddress;

        public RecommendationHolder(@NonNull View itemView) {
            super(itemView);
            ivRecommendation = itemView.findViewById(R.id.ivRecommendation);
            tvRecommendationName = itemView.findViewById(R.id.tvRecommendationFoodName);
            tvRecommendationAddress = itemView.findViewById(R.id.tvRecommendationAddress);
        }
    }
}
