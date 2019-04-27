package edu.nyu.gobble.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

import edu.nyu.gobble.R;
import edu.nyu.gobble.config.Config;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private EditText etFirstName,etLastName,etemail,etPassword,etConfirmPassword;
    private Button btRegister,btGoToSignin;
    private static final String registerUrl = Config.baseUrl + "register";
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private String confirmPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etemail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etRegisterConfirmPassword);

        btRegister = findViewById(R.id.btRegister);
        btGoToSignin = findViewById(R.id.btGoToSignin);

        btRegister.setOnClickListener(this);
        btGoToSignin.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btRegister:
                firstName = etFirstName.getText().toString();
                lastName = etLastName.getText().toString();
                emailId = etemail.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();

                if (firstName.equals("") || lastName.equals("") || password.equals("") || confirmPassword.equals("") || emailId.equals("")){
                    Toast.makeText(Registration.this,"All Fields are required",Toast.LENGTH_SHORT).show();
                }else if (!password.equals(confirmPassword)){
                    Toast.makeText(Registration.this,"Passwords does not match",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Registration.this,"Registering. . .",Toast.LENGTH_SHORT).show();
                    new RegisterUser().execute("");
                }



                break;

            case R.id.btGoToSignin:
                Intent registrationIntent = new Intent(Registration.this,Login.class);
                startActivity(registrationIntent);
                break;
        }
    }

    class RegisterUser extends AsyncTask<String,String,String>{

        HttpURLConnection connection;
        InputStream inputStream;
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        OutputStream outputStream;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {


            try {
                URL url = new URL(registerUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                outputStream =  connection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("firstName","UTF-8")+"="+URLEncoder.encode(firstName,"UTF-8")+"&"
                        +URLEncoder.encode("lastName","UTF-8")+"="+URLEncoder.encode(lastName,"UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emailId,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

                bufferedWriter.write(post_data);

                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    sb.append(line + "\n");
                }

                if (sb.length() == 0)
                    return "Problem while connecting with server";

                result  = getDataFromJson(sb.toString());

                bufferedReader.close();
                inputStream.close();
                connection.disconnect();




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
                Toast.makeText(Registration.this,"Successfully Registered",Toast.LENGTH_SHORT).show();
            }else if (s.equals("false")){
                Toast.makeText(Registration.this,"Registration Not Successful",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getDataFromJson(String jsonData){
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
