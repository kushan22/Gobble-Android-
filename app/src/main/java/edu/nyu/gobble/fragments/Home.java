package edu.nyu.gobble.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import edu.nyu.gobble.AddFriends;
import edu.nyu.gobble.AddTrips;
import edu.nyu.gobble.R;
import edu.nyu.gobble.adapters.TripAdapter;
import edu.nyu.gobble.config.Config;
import edu.nyu.gobble.pojo.TripModel;
import edu.nyu.gobble.registration.Login;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener {

    private RecyclerView rvTrips;
    private LinearLayoutManager linearLayoutManager;
    private TripAdapter tripAdapter;
    private ArrayList<TripModel> tripDetails = new ArrayList<>();

    private ImageButton ibAddTrip;
    private FloatingActionButton fbAddFriends,fbAddTrips;

    private static final String tripDetailsUrl  = Config.baseUrl + "getTripDetails";
    private String userId;

    private RelativeLayout rl1;
    private LinearLayout ll1;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_fragment1,container,false);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Login.PREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Login.USERID,"none");

        ll1 = view.findViewById(R.id.ll1);
        rl1 = view.findViewById(R.id.rl1);

        rvTrips = view.findViewById(R.id.rvUpComingTrips);
        rvTrips.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvTrips.setLayoutManager(linearLayoutManager);

        new GetTripDetails().execute("");







        ibAddTrip = view.findViewById(R.id.ibAddTrip);
        ibAddTrip.setOnClickListener(this);

        fbAddFriends = view.findViewById(R.id.fbAddFriends);
        fbAddFriends.setOnClickListener(this);

        fbAddTrips = view.findViewById(R.id.fbAddTrips);
        fbAddTrips.setOnClickListener(this);



        return view;
    }

//    private void addTripDetails(){
//        for (int i = 1; i <= 5; i++){
//            TripModel tripModel = new TripModel();
//            tripModel.setTripCity("City " + i);
//            tripModel.setTripDate("Date " + i);
//
//            tripDetails.add(tripModel);
//        }
//    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.ibAddTrip:
                Intent addTripIntent = new Intent(getContext(), AddTrips.class);
                startActivity(addTripIntent);
                break;

            case R.id.fbAddFriends:
                Intent addFriendsIntent = new Intent(getContext(), AddFriends.class);
                startActivity(addFriendsIntent);
                break;
            case R.id.fbAddTrips:

                Intent intent = new Intent(getContext(),AddTrips.class);
                startActivity(intent);
                break;
        }

    }

    private class GetTripDetails extends AsyncTask<String,String,ArrayList<TripModel>>{

        HttpURLConnection request;
        BufferedReader bufferedReader;
        InputStream inputStream;
        ArrayList<TripModel> result = new ArrayList<>();


        @Override
        protected ArrayList<TripModel> doInBackground(String... strings) {
            Uri uri = Uri.parse(tripDetailsUrl).buildUpon().appendQueryParameter("userid",userId).build();
            try {
                URL url = new URL(uri.toString());
                request = (HttpURLConnection) url.openConnection();
                request.setRequestMethod("GET");

                request.connect();

                inputStream = request.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                    sb.append(line + "\n");
                }

                result = getDataFromJson(sb.toString());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                request.disconnect();
                try {
                    inputStream.close();
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<TripModel> tripModels) {
            super.onPostExecute(tripModels);

            if (tripModels.isEmpty()){
               ll1.setVisibility(View.VISIBLE);
               rl1.setVisibility(View.INVISIBLE);
            }else{
                ll1.setVisibility(View.INVISIBLE);
                rl1.setVisibility(View.VISIBLE);

                tripAdapter = new TripAdapter(getContext(),tripModels);
                rvTrips.setAdapter(tripAdapter);



            }
        }
    }

    private ArrayList<TripModel> getDataFromJson(String jsonData){
        ArrayList<TripModel> tripDetailsList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject.getString("success").equals("true")){
                JSONArray jsonArray = jsonObject.getJSONArray("tripdetails");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    TripModel tripModel = new TripModel();
                    tripModel.setTripCity(jsonObject1.getString("tripCity"));
                    tripModel.setTripDate(jsonObject1.getString("date"));
                    tripDetailsList.add(tripModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tripDetailsList;
    }
}
