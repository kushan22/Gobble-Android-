package edu.nyu.gobble.registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import edu.nyu.gobble.Home;
import edu.nyu.gobble.R;
import edu.nyu.gobble.SetProfile;
import edu.nyu.gobble.config.Config;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail,etPassword;
    private Button btSignIn,btgoToRegister;
    private String email,password;
    private static final String loginUrl = Config.baseUrl + "login";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String PREFNAME = "loginPref";
    public static final String EMAIL = "loginEmail";
    public static final String PASSWORD = "password";
    public static final String FULLNAME = "fullName";
    public static final String USERID = "userid";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etEmail = findViewById(R.id.etSigninEmail);
        etPassword = findViewById(R.id.etSigninPassword);

        etEmail.setText("kushansingh22@gmail.com");
        etPassword.setText("1234");

        btSignIn = findViewById(R.id.btSignin);
        btgoToRegister = findViewById(R.id.btgoToRegister);

        btSignIn.setOnClickListener(this);
        btgoToRegister.setOnClickListener(this);

        makeToast("Inside Login");

    }

    private void makeToast(String message){
        Toast.makeText(Login.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btSignin:
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if (email.equals("") || password.equals("")){
                    makeToast("All fields are required");
                }else{
                    new Authenticate().execute("");
                }



                break;
            case R.id.btgoToRegister:

                Intent loginIntent = new Intent(Login.this,Registration.class);
                startActivity(loginIntent);

                break;
        }
    }

    class Authenticate extends AsyncTask<String,String,String>{

        HttpURLConnection request;
        BufferedWriter bufferedWriter;
        OutputStream outputStream;
        BufferedReader bufferedReader;
        InputStream inputStream;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(loginUrl);
                request = (HttpURLConnection) url.openConnection();
                request.setRequestMethod("POST");
                request.setDoInput(true);
                request.setDoOutput(true);

                outputStream = request.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

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

            if (s.equals("false")){
                makeToast("Cannot Authenticate");
            }else{

                String[] result = s.split(",");
                String fullName = result[0];
                String userId = result[1];

                sharedPreferences = getSharedPreferences(PREFNAME,Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString(EMAIL,email);
                editor.putString(PASSWORD,password);
                editor.putString(FULLNAME,fullName);
                editor.putString(USERID,userId);

                editor.commit();

                Intent intent = new Intent(Login.this, SetProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //makeToast(s);
            }
        }
    }

    private String getDataFromJson(String jsonData){
        Log.i("JSONDATA",jsonData);
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject.getString("success").equals("true")){
                result = jsonObject.getString("fullname") + "," +  jsonObject.getString("uid");
            }else{
                result = "false";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("MYRESULT",result);
        return result;
    }
}
