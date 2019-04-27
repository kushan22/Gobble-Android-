package edu.nyu.gobble.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nyu.gobble.R;
import edu.nyu.gobble.Recommendation;
import edu.nyu.gobble.pojo.TripModel;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {

    private Context context;
    private ArrayList<TripModel> tripDetails;

    public TripAdapter(Context context, ArrayList<TripModel> tripDetails) {
        this.context = context;
        this.tripDetails = tripDetails;
    }

    @NonNull
    @Override
    public TripAdapter.TripHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.card_trips,viewGroup,false);
        TripHolder tripHolder = new TripHolder(v);
        return tripHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.TripHolder tripHolder, int i) {

        final TripModel tripModel = tripDetails.get(i);
        tripHolder.tvTripCity.setText(tripModel.getTripCity());
        tripHolder.tvTripDate.setText(tripModel.getTripDate());

        tripHolder.ibRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Recommendation.class);
                intent.putExtra("CITY",tripModel.getTripCity());
                intent.putExtra("DATE",tripModel.getTripDate());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tripDetails.size();
    }

    class TripHolder extends RecyclerView.ViewHolder{

        private TextView tvTripCity,tvTripDate;
        private ImageButton ibRecommend;

        public TripHolder(@NonNull View itemView) {
            super(itemView);

            tvTripCity = itemView.findViewById(R.id.tvTripCity);
            tvTripDate = itemView.findViewById(R.id.tvTripDate);
            ibRecommend = itemView.findViewById(R.id.ibRecommend);
        }
    }
}
