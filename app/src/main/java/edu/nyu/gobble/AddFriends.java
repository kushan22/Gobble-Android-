package edu.nyu.gobble;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import edu.nyu.gobble.adapters.AddFriendsAdapter;
import edu.nyu.gobble.config.Config;
import edu.nyu.gobble.registration.Login;

public class AddFriends extends AppCompatActivity {

    private RecyclerView rvAddFriends;
    private LinearLayoutManager linearLayoutManager;
    private AddFriendsAdapter addFriendsAdapter;
    private String userid;



    private static final String usersUrl = Config.baseUrl + "getUsers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFNAME, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString(Login.USERID,"none");

        rvAddFriends = findViewById(R.id.rvFriends);
        rvAddFriends.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(AddFriends.this);
        rvAddFriends.setLayoutManager(linearLayoutManager);


        new GetAllUsers().execute("");



    }

    private class GetAllUsers extends AsyncTask<String,String,String>{

        HttpURLConnection request;
        BufferedReader bufferedReader;
        InputStream inputStream;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {
            Uri uri = Uri.parse(usersUrl).buildUpon().appendQueryParameter("userid",userid).build();
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
//            Toast.makeText(AddFriends.this,s,Toast.LENGTH_SHORT).show();

            String[] results = s.split(":");

            ArrayList<String> userIds = new ArrayList<>(Arrays.asList(results[0].split(",")));
            ArrayList<String> fullNames = new ArrayList<>(Arrays.asList(results[1].split(",")));

            addFriendsAdapter = new AddFriendsAdapter(AddFriends.this,userIds,fullNames);
            rvAddFriends.setAdapter(addFriendsAdapter);
        }
    }

    private String getDataFromJson(String jsonData){

        Log.i("JSONFRIENDS",jsonData);

        String res = "";
        String userIds = "";
        String fullNames = "";



        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("users");

            Log.i("JSON", String.valueOf(jsonArray.length()));


            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Log.i("JSONOBJECT",jsonObject1.toString());
                if (i == 0){
                    userIds = userIds + jsonObject1.getString("uid");
                    fullNames = fullNames + jsonObject1.getString("fullName");
                }else{
                    userIds = userIds + "," + jsonObject1.getString("uid");
                    fullNames = fullNames + "," +  jsonObject1.getString("fullName");
                }
            }

            res = userIds + ":" + fullNames;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

}
