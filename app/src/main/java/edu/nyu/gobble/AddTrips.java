package edu.nyu.gobble;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import edu.nyu.gobble.adapters.AddConsiderationAdapter;
import edu.nyu.gobble.adapters.AddDislikeAdapter;
import edu.nyu.gobble.adapters.AddPreferenceAdapter;
import edu.nyu.gobble.adapters.PreferencesAdapter;
import edu.nyu.gobble.adapters.TripAddFriendsAdapter;
import edu.nyu.gobble.config.Config;
import edu.nyu.gobble.pojo.AddFriendModel;
import edu.nyu.gobble.pojo.Model;
import edu.nyu.gobble.registration.Login;

public class AddTrips extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView rvPreferences,rvDislikes,rvConsiderations;
    private LinearLayoutManager linearLayoutManager1,linearLayoutManager2,linearLayoutManager3;
    private AddPreferenceAdapter preferenceAdapter;
    private AddDislikeAdapter dislikeAdapter;
    private AddConsiderationAdapter considerationAdapter;

    private static final String getPreferencesUrl = Config.baseUrl + "getPreferences";
    private static final String friendListUrl = Config.baseUrl + "getFriendList";
    private static final String addTripUrl = Config.baseUrl + "addTripDetails";
    private String userId;

    private EditText etFromDate,etToDate;
    private DatePickerDialog.OnDateSetListener fromDatePickerDialog, toDatePickerDialog;
    private Calendar fromCalendar,toCalendar;
    private FloatingActionButton fabFriends;
    private PopupWindow popupWindow;
    private ArrayList<AddFriendModel> addFriends;
    private String[] fullNames;
    private String[] friendsUserIds;

    private TripAddFriendsAdapter tripAddFriendsAdapter;
    private RelativeLayout relativeLayout;
    private View v;
    private RecyclerView rvAddTripFriends;

    private String friendsGroup;
    private String friendsGroupUserIds;

    private EditText etTripCity;
    private Button btCreateTrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trips);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(Login.PREFNAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Login.USERID,"none");

        rvPreferences = findViewById(R.id.rvPreference);
        rvDislikes = findViewById(R.id.rvDislike);
        rvConsiderations = findViewById(R.id.rvConsideration);

        rvPreferences.setHasFixedSize(true);
        linearLayoutManager1 = new LinearLayoutManager(AddTrips.this,LinearLayoutManager.HORIZONTAL,false);
        rvPreferences.setLayoutManager(linearLayoutManager1);

        rvDislikes.setHasFixedSize(true);
        linearLayoutManager2 = new LinearLayoutManager(AddTrips.this,LinearLayoutManager.HORIZONTAL,false);
        rvDislikes.setLayoutManager(linearLayoutManager2);

        rvConsiderations.setHasFixedSize(true);
        linearLayoutManager3 = new LinearLayoutManager(AddTrips.this,LinearLayoutManager.HORIZONTAL,false);
        rvConsiderations.setLayoutManager(linearLayoutManager3);

        etFromDate = findViewById(R.id.etFromDate);
        etToDate = findViewById(R.id.etToDate);

        etFromDate.setOnClickListener(this);
        etToDate.setOnClickListener(this);

        fromCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                fromCalendar.set(Calendar.YEAR, i);
                fromCalendar.set(Calendar.MONTH, i1);
                fromCalendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabelFromDate();


            }
        };

        toCalendar = Calendar.getInstance();
        toDatePickerDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                toCalendar.set(Calendar.YEAR, i);
                toCalendar.set(Calendar.MONTH, i1);
                toCalendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabelToDate();
            }
        };

        fabFriends = findViewById(R.id.fbAddFriends);
        fabFriends.setOnClickListener(this);

        etTripCity = findViewById(R.id.etTripCity);
        btCreateTrip = findViewById(R.id.btCreateTrip);
        btCreateTrip.setOnClickListener(this);


        new GetPreferencesData().execute("");

    }

    private void updateLabelFromDate(){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etFromDate.setText(sdf.format(fromCalendar.getTime()));
    }

    private void updateLabelToDate(){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etToDate.setText(sdf.format(toCalendar.getTime()));
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.etFromDate:

                new DatePickerDialog(AddTrips.this,fromDatePickerDialog,fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH)).show();



                break;

            case R.id.etToDate:

                new DatePickerDialog(AddTrips.this,toDatePickerDialog,toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH)).show();


                break;

            case R.id.fbAddFriends:

                v = LayoutInflater.from(AddTrips.this).inflate(R.layout.custom_tripaddfriends,null,false);
                relativeLayout = v.findViewById(R.id.rlTripAddFriends);

                rvAddTripFriends = v.findViewById(R.id.rvTripAddFriends);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddTrips.this);
                rvAddTripFriends.setHasFixedSize(true);
                rvAddTripFriends.setLayoutManager(linearLayoutManager);

                Button btTripAddFriends= v.findViewById(R.id.btTripAddFriends);

                new GetAllFriends().execute("");

                btTripAddFriends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int i = 0;
                        for (AddFriendModel model: addFriends){
                            if (model.isSelected()){
                                if (i == 0){
                                    friendsGroup = friendsGroup + model.getFullName();
                                    friendsGroupUserIds = friendsGroupUserIds + model.getUserId();
                                }else{
                                    friendsGroup = friendsGroup + "," + model.getFullName();
                                    friendsGroupUserIds = friendsGroupUserIds + "," + model.getUserId();
                                }
                                i++;
                            }
                        }

                        popupWindow.dismiss();
                    }
                });

                break;
                case R.id.btCreateTrip:

                    String tripCity = etTripCity.getText().toString();
                    String fromDate = etFromDate.getText().toString();
                    String toDate = etToDate.getText().toString();

                    new AddTripDetails().execute(tripCity,fromDate,toDate);




                break;
        }

    }




    private class GetPreferencesData extends AsyncTask<String,String,String>{

        HttpURLConnection request;
        BufferedReader bufferedReader;
        InputStream inputStream;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {


                Uri uri = Uri.parse(getPreferencesUrl).buildUpon().appendQueryParameter("userid",userId).build();

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
                Toast.makeText(AddTrips.this,"No Preferences Set",Toast.LENGTH_SHORT).show();
            }else{
                String[] results = s.split(":");
                ArrayList<String> likesList = new ArrayList<>(Arrays.asList(results[0].split(",")));
                ArrayList<String> dislikesList = new ArrayList<>(Arrays.asList(results[1].split(",")));
                ArrayList<String> considerationList = new ArrayList<>(Arrays.asList(results[2].split(",")));

                preferenceAdapter = new AddPreferenceAdapter(AddTrips.this,likesList);
                rvPreferences.setAdapter(preferenceAdapter);

                dislikeAdapter = new AddDislikeAdapter(AddTrips.this,dislikesList);
                rvDislikes.setAdapter(dislikeAdapter);

                considerationAdapter = new AddConsiderationAdapter(AddTrips.this,considerationList);
                rvConsiderations.setAdapter(considerationAdapter);


            }
        }
    }

    private String getDataFromJson(String jsonData){
        String res = "";
        //Log.i("JSONPREFERENCES",jsonData);
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject.getString("success").equals("false")){
                res = "false";
            }else{
                res = jsonObject.getString("likes") + ":" + jsonObject.getString("dislikes") + ":" + jsonObject.getString("considerations");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return res;
    }

    private class GetAllFriends extends AsyncTask<String,String,String>{

        HttpURLConnection request;
        BufferedReader bufferedReader;
        InputStream inputStream;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {
            Uri uri = Uri.parse(friendListUrl).buildUpon().appendQueryParameter("userid",userId).build();
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

                result = getriendsDataFromJson(sb.toString());


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

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("false")){

            }else{
                String[] result = s.split(":");
                fullNames = result[0].split(",");
                friendsUserIds = result[1].split(",");
                tripAddFriendsAdapter = new TripAddFriendsAdapter(AddTrips.this,getFriendsData());

                popupWindow = new PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setFocusable(true);
                popupWindow.update();
                popupWindow.showAtLocation(relativeLayout,Gravity.CENTER,0,0);
                rvAddTripFriends.setAdapter(tripAddFriendsAdapter);


            }
        }
    }

    private String getriendsDataFromJson(String jsonData){

        String res = "";
        String userIds = "";
        String fullNames = "";

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            Log.i("FRIENDLISTJSON",jsonObject.toString());
            if (jsonObject.getString("success").equals("true")){
                JSONArray jsonArray = jsonObject.getJSONArray("users");

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    if (i == 0){

                        fullNames = fullNames + jsonObject1.getString("fullName");
                        userIds = userIds + jsonObject1.getString("uid");
                    }else{

                        fullNames = fullNames + "," +  jsonObject1.getString("fullName");
                        userIds = userIds + "," +  jsonObject1.getString("uid");
                    }
                }
                res = fullNames + ":" + userIds;



            }else{
                res = "false";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    private ArrayList<AddFriendModel> getFriendsData() {
        addFriends = new ArrayList<>();
        for (int i = 0; i < fullNames.length; i++) {
            addFriends.add(new AddFriendModel(fullNames[i],friendsUserIds[i]));
        }
        return addFriends;
    }

    private class AddTripDetails extends AsyncTask<String,String,String>{

        HttpURLConnection request;
        BufferedWriter bufferedWriter;
        OutputStream outputStream;
        BufferedReader bufferedReader;
        InputStream inputStream;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(addTripUrl);
                request = (HttpURLConnection) url.openConnection();
                request.setRequestMethod("POST");
                request.setDoInput(true);
                request.setDoOutput(true);

                outputStream = request.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userId,"UTF-8")+"&"+URLEncoder.encode("groupids","UTF-8")+"="+URLEncoder.encode(friendsGroupUserIds,"UTF-8")+"&"+URLEncoder.encode("cityName","UTF-8")+"="+URLEncoder.encode(strings[0],"UTF-8")+"&"+URLEncoder.encode("fromDate","UTF-8")+"="+URLEncoder.encode(strings[1],"UTF-8")+"&"+URLEncoder.encode("toDate","UTF-8")+"="+URLEncoder.encode(strings[2],"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = request.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = "";

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line + "\n");
                }

                result = getTripDetailsDataFromJson(sb.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("true")){
                Toast.makeText(AddTrips.this,"Trip Added Successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddTrips.this,Home.class);
                startActivity(intent);
            }else{
                Toast.makeText(AddTrips.this,"Trip Couldn't be added",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getTripDetailsDataFromJson(String jsonData){

       String result = "";

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            result = jsonObject.getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }


}
