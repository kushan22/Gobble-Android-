package edu.nyu.gobble.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.Arrays;

import edu.nyu.gobble.R;
import edu.nyu.gobble.adapters.FriendRequestAdapter;
import edu.nyu.gobble.config.Config;
import edu.nyu.gobble.registration.Login;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendRequets extends Fragment {

    private RecyclerView rvFriendRequests;
    private LinearLayoutManager linearLayoutManager;
    private FriendRequestAdapter friendRequestAdapter;

    private static final String friendRequestUrl = Config.baseUrl + "getFriendRequests";

    private String userId;

    public FriendRequets() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_friend_requets, container, false);

        rvFriendRequests = v.findViewById(R.id.rvFriendRequests);
        rvFriendRequests.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvFriendRequests.setLayoutManager(linearLayoutManager);

        SharedPreferences preferences = getContext().getSharedPreferences(Login.PREFNAME, Context.MODE_PRIVATE);
        userId = preferences.getString(Login.USERID,"none");

        new GetFriendRequests().execute("");

        return v;
    }

    private class GetFriendRequests extends AsyncTask<String,String,String>{

        HttpURLConnection request;
        BufferedReader bufferedReader;
        InputStream inputStream;
        String result = "";


        @Override
        protected String doInBackground(String... strings) {
            Uri uri = Uri.parse(friendRequestUrl).buildUpon().appendQueryParameter("userid",userId).build();
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("false")){
                Toast.makeText(getContext(),"No Friend Requests",Toast.LENGTH_SHORT).show();
            }else{
                String[] results = s.split(":");

                ArrayList<String> userIds = new ArrayList<>(Arrays.asList(results[0].split(",")));
                ArrayList<String> fullNames = new ArrayList<>(Arrays.asList(results[1].split(",")));

                friendRequestAdapter = new FriendRequestAdapter(getContext(),userIds,fullNames);
                rvFriendRequests.setAdapter(friendRequestAdapter);
            }

        }
    }

    private String getDataFromJson(String jsonData){
        String res = "";
        String userIds = "";
        String fullNames = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject.getString("success").equals("true")){
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    if (i == 0){
                        userIds = userIds + jsonObject1.getString("uid");
                        fullNames = fullNames + jsonObject1.getString("fullName");
                    }else{
                        userIds = userIds + "," + jsonObject1.getString("uid");
                        fullNames = fullNames + "," +  jsonObject1.getString("fullName");
                    }
                }

                res = userIds + ":" + fullNames;

            }else {
                res = "false";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

}
