package edu.nyu.gobble;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.nyu.gobble.adapters.ConsiderationsAdapter;
import edu.nyu.gobble.adapters.DislikeAdapter;
import edu.nyu.gobble.adapters.PreferencesAdapter;
import edu.nyu.gobble.config.Config;
import edu.nyu.gobble.pojo.Model;
import edu.nyu.gobble.registration.Login;

public class SetProfile extends AppCompatActivity implements View.OnClickListener {

    private Button btSetPreferences,btSetDislikes,btSetConsiderations,btSaveProfile;
    private TextView tvSetStatus;
    private CircleImageView cvProfileImage;
    private PopupWindow popupWindow;
    private ArrayList<Model> preferences = new ArrayList<>();
    private ArrayList<Model> dislikes = new ArrayList<>();
    private ArrayList<Model> considerations = new ArrayList<>();

    public static final String PREFCONSIDERATION = "prefConsideration";
    public static final String PREFPREFERENCES = "prefPreerences";
    public static final String PREFDISLIKES = "prefDislikes";

    public static final String PREFERENCESSTRING = "preferences";
    public static final String DISLIKESTRING = "dislikes";
    public static final String CONSIDERATIONSTRING = "considerations";
    private String savedConsiderations;
    private String savedPreferences;
    private String savedDislikes;
    private String userId;


    public static final String preferencesUrl = Config.baseUrl + "savePreferences";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Boolean isFirstRun = getSharedPreferences("PREFERENCES",Context.MODE_PRIVATE).getBoolean("isFirstRun",true);

        if (!isFirstRun){
            Intent intent = new Intent(SetProfile.this,Home.class);
            startActivity(intent);
        }else{

            btSetPreferences = findViewById(R.id.btPreferences);
            btSetDislikes = findViewById(R.id.btDislikes);
            btSetConsiderations = findViewById(R.id.btConsiderations);
            btSaveProfile = findViewById(R.id.btSaveProfile);

            tvSetStatus = findViewById(R.id.tvStatus);
            cvProfileImage = findViewById(R.id.profile_image);

            btSetPreferences.setOnClickListener(this);
            btSetDislikes.setOnClickListener(this);
            btSetConsiderations.setOnClickListener(this);
            btSaveProfile.setOnClickListener(this);


            SharedPreferences preferences = getSharedPreferences(Login.PREFNAME,Context.MODE_PRIVATE);
            userId = preferences.getString(Login.USERID,"none");

        }

        getSharedPreferences("PREFERENCES",MODE_PRIVATE).edit().putBoolean("isFirstRun",false).commit();



    }

    private ArrayList<Model> getPreferencesListData() {
        preferences = new ArrayList<>();
        preferences.add(new Model("Pizza"));
        preferences.add(new Model("Bagel"));
        preferences.add(new Model("Lobster"));
        preferences.add(new Model("Sushi"));
        preferences.add(new Model("Pancakes"));
        preferences.add(new Model("Spicy"));
        preferences.add(new Model("Donuts"));


        return preferences;
    }

    private ArrayList<Model> getDislikesData() {
        dislikes = new ArrayList<>();

        dislikes.add(new Model("Fish"));
        dislikes.add(new Model("Beef"));
        dislikes.add(new Model("Oily Food"));
        dislikes.add(new Model("Low Hygiene"));
        dislikes.add(new Model("Bean Sprouts"));
        dislikes.add(new Model("Peas"));


        return dislikes;
    }

    private ArrayList<Model> getConsiderationData() {
        considerations = new ArrayList<>();

            considerations.add(new Model("Vegetarian "));
            considerations.add(new Model("Dog Friendly "));
            considerations.add(new Model("Halal "));
            considerations.add(new Model("Wheelchair accessible"));
            considerations.add(new Model("Lifts"));
            considerations.add(new Model("Shortest Walking Distance"));
            considerations.add(new Model("Budget"));
            considerations.add(new Model("Air Conditioning"));

        return considerations;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btPreferences:
                View preferenceView = LayoutInflater.from(SetProfile.this).inflate(R.layout.custom_preferences,null,false);
                final RelativeLayout relativeLayout = preferenceView.findViewById(R.id.rlPreferences);

                RecyclerView rvPreferences = preferenceView.findViewById(R.id.rvPreferences);
                PreferencesAdapter mAdapter = new PreferencesAdapter(SetProfile.this,  getPreferencesListData());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SetProfile.this);
                rvPreferences.setHasFixedSize(true);
                rvPreferences.setLayoutManager(linearLayoutManager);

                Button btSavePreferences= preferenceView.findViewById(R.id.btPreferences);




                popupWindow = new PopupWindow(preferenceView, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setFocusable(true);
                popupWindow.update();
                popupWindow.showAsDropDown(relativeLayout,0,0,Gravity.CENTER);
                rvPreferences.setAdapter(mAdapter);

                btSavePreferences.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        savedPreferences = "";
                        int i = 0;
                        for (Model model: preferences){
                                if (model.isSelected()){
                                    if (i == 0){
                                        savedPreferences = savedPreferences + model.getText();
                                    }else{
                                        savedPreferences  = savedPreferences + "," + model.getText();
                                    }
                                    i++;
                                }
                        }

//                        SharedPreferences prefPreferences = getSharedPreferences(PREFERENCESSTRING, Context.MODE_PRIVATE);
//                        SharedPreferences.Editor preferencesEditor = prefPreferences.edit();
//                        preferencesEditor.putString(CONSIDERATIONSTRING,savedPreferences);
//                        preferencesEditor.commit();

                        if (savedPreferences.equals("")){
                            Toast.makeText(SetProfile.this,"You need to select at least one",Toast.LENGTH_SHORT).show();
                        }else{
                            Log.i("PREFERENCES",savedPreferences);
                            popupWindow.dismiss();
                        }


                    }
                });






                break;

            case R.id.btDislikes:
                View dislikeView = LayoutInflater.from(SetProfile.this).inflate(R.layout.custom_dislikes,null,false);
                final RelativeLayout relativeLayout1 = dislikeView.findViewById(R.id.rlDislikes);

                RecyclerView rvDislikes = dislikeView.findViewById(R.id.rvDislikes);
                DislikeAdapter dislikeAdapter = new DislikeAdapter(SetProfile.this,  getDislikesData());
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(SetProfile.this);
                rvDislikes.setHasFixedSize(true);
                rvDislikes.setLayoutManager(linearLayoutManager1);


                Button btDislikesSave = dislikeView.findViewById(R.id.btDislikes);



                popupWindow = new PopupWindow(dislikeView, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setFocusable(true);
                popupWindow.update();
                popupWindow.showAsDropDown(relativeLayout1,0,0,Gravity.CENTER);
                rvDislikes.setAdapter(dislikeAdapter);

                btDislikesSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        savedDislikes = "";
                        int i = 0;
                        for (Model model: dislikes){
                            if (model.isSelected()){
                                if (i == 0){
                                    savedDislikes = savedDislikes + model.getText();
                                }else{
                                    savedDislikes  = savedDislikes + "," + model.getText();
                                }
                                i++;
                            }
                        }

                        if (savedDislikes.equals("")){
                            Toast.makeText(SetProfile.this,"You need to select at least one",Toast.LENGTH_SHORT).show();
                        }else {

//                        SharedPreferences prefDislikes = getSharedPreferences(PREFDISLIKES, Context.MODE_PRIVATE);
//                        SharedPreferences.Editor dislikesEditor = prefDislikes.edit();
//                        dislikesEditor.putString(CONSIDERATIONSTRING,savedDislikes);
//                        dislikesEditor.commit();

                            Log.i("PREFERENCES", savedDislikes);
                            popupWindow.dismiss();
                        }
                    }
                });

                break;

            case R.id.btConsiderations:
                View considerationView = LayoutInflater.from(SetProfile.this).inflate(R.layout.custom_considerations,null,false);
                final RelativeLayout relativeLayout2 = considerationView.findViewById(R.id.rlConsiderations);

                RecyclerView rvConsiderations = considerationView.findViewById(R.id.rvConsiderations);
                ConsiderationsAdapter considerationsAdapter = new ConsiderationsAdapter(SetProfile.this,  getConsiderationData());
                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(SetProfile.this);
                rvConsiderations.setHasFixedSize(true);
                rvConsiderations.setLayoutManager(linearLayoutManager2);

                Button btConsiderationsSave = considerationView.findViewById(R.id.btConsiderations);

                popupWindow = new PopupWindow(considerationView, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.setFocusable(true);
                popupWindow.update();
                popupWindow.setBackgroundDrawable(SetProfile.this.getDrawable(R.drawable.bg_navigation));
                popupWindow.showAsDropDown(relativeLayout2,0,0,Gravity.CENTER);
                rvConsiderations.setAdapter(considerationsAdapter);

                btConsiderationsSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        savedConsiderations = "";
                        int i = 0;
                        for (Model model: considerations){
                            if (model.isSelected()){
                                if (i == 0){
                                    savedConsiderations = savedConsiderations + model.getText();
                                }else{
                                    savedConsiderations  = savedConsiderations + "," + model.getText();
                                }
                                i++;
                            }
                        }

//                        SharedPreferences prefConsideration = getSharedPreferences(PREFCONSIDERATION, Context.MODE_PRIVATE);
//                        SharedPreferences.Editor considerationEditor = prefConsideration.edit();
//                        considerationEditor.putString(CONSIDERATIONSTRING,savedConsiderations);
//                        considerationEditor.commit();

                        if (savedConsiderations.equals("")){
                            Toast.makeText(SetProfile.this,"You need to select at least one",Toast.LENGTH_SHORT).show();
                        }else{
                            Log.i("PREFERENCES",savedConsiderations);
                            popupWindow.dismiss();
                        }




                    }
                });

                break;
            case R.id.btSaveProfile:

                new SaveUserPreferences().execute("");

                break;
        }

    }

    private class SaveUserPreferences extends AsyncTask<String,String,String>{

        HttpURLConnection connection;
        BufferedWriter writer;
        BufferedReader reader;
        InputStream inputStream;
        OutputStream outputStream;
        String result;

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(preferencesUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                outputStream = connection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("likes","UTF-8")+"="+URLEncoder.encode(savedPreferences,"UTF-8")+"&"+URLEncoder.encode("dislikes","UTF-8")+"="+URLEncoder.encode(savedDislikes,"UTF-8")+"&"+URLEncoder.encode("considerations","UTF-8")+"="+URLEncoder.encode(savedConsiderations,"UTF-8")+"&"+URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(userId,"UTF-8");

                writer.write(post_data);

                writer.flush();
                writer.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }

                result = getDataFromJson(sb.toString());



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
            Toast.makeText(SetProfile.this,s,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SetProfile.this,Home.class);
            startActivity(intent);
        }
    }


    private String getDataFromJson(String jsonData){
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject.getString("success").equals("true")){
                result = "true";
            }else{
                result = "false";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }
}
