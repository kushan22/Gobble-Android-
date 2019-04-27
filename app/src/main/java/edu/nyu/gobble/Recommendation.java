package edu.nyu.gobble;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nyu.gobble.adapters.RecommendationAdapter;
import edu.nyu.gobble.pojo.RecommendationModel;

public class Recommendation extends AppCompatActivity {

    private RecyclerView rvRecommendations;
    private LinearLayoutManager linearLayoutManager;
    private RecommendationAdapter recommendationAdapter;

    private TextView tvCity,tvDate;
    private String cityName,date;

    private ArrayList<RecommendationModel> recommendationModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addRecommendations();

        Intent receiverIntent = getIntent();
        cityName = receiverIntent.getStringExtra("CITY");
        date = receiverIntent.getStringExtra("DATE");

        tvCity = findViewById(R.id.tvRecommendationCity);
        tvDate = findViewById(R.id.tvRecommendationDate);

        tvCity.setText(cityName);
        tvDate.setText(date);


        rvRecommendations = findViewById(R.id.rvRecommendation);
        linearLayoutManager = new LinearLayoutManager(Recommendation.this);
        rvRecommendations.setLayoutManager(linearLayoutManager);

        recommendationAdapter = new RecommendationAdapter(Recommendation.this,recommendationModels);
        rvRecommendations.setAdapter(recommendationAdapter);


    }

    private void addRecommendations(){
        Integer[] images = new Integer[]{R.drawable.r_blr_by_shojo_food,R.drawable.r_carmelinas_food,R.drawable.r_mikes_diner_food,R.drawable.r_pho_basil_food,R.drawable.r_the_corner_boston_food};
        String[] restaurantNames = new String[]{"BLR By Shojo","Carmelinaís","Mikeís City Diner","Pho Basil","The Corner Pub"};
        String[] addresses = new String[]{"13A Hudson St\n" +
                "boston, MA 02111\n" +
                "b/t Ping On St & Beach St \n" +
                "Chinatown","307 Hanover St\n" +
                "boston, MA 02113\n" +
                "b/t Lathro","1714 Washington St\n" +
                "boston, MA 02118\n" +
                "b/t Worcester Sq & Springfield St \n" +
                "South End",
                "177 Massachusetts Ave\n" +
                        "Ste A\n" +
                        "boston, MA 02115\n" +
                        "b/t Clearway St & St Germain St",
                "162 Lincoln St\n" +
                        "boston, MA 02111\n" +
                        "b/t Beach St & Utica Pl \n" +
                        "Leather District, Waterfront, South boston"
        };

        for (int i = 0; i < images.length; i++){
            RecommendationModel recommendationModel = new RecommendationModel();
            recommendationModel.setImageName(images[i]);
            recommendationModel.setAddress(addresses[i]);
            recommendationModel.setName(restaurantNames[i]);

            recommendationModels.add(recommendationModel);
        }
    }

}
