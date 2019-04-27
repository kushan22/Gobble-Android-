package edu.nyu.gobble.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import edu.nyu.gobble.R;
import edu.nyu.gobble.config.Config;
import edu.nyu.gobble.registration.Login;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestHolder> {

    private Context context;
    private ArrayList<String> userIds,fullNames;
    private String userId;
    private static final String acceptFriendReqUrl = Config.baseUrl + "acceptFriendReq";
    public FriendRequestAdapter(Context context, ArrayList<String> userIds, ArrayList<String> fullNames) {

        this.context = context;
        this.userIds = userIds;
        this.fullNames = fullNames;
    }

    @NonNull
    @Override
    public FriendRequestAdapter.FriendRequestHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_friend_requests,viewGroup,false);
        FriendRequestHolder friendRequestHolder = new FriendRequestHolder(v);

        return friendRequestHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestAdapter.FriendRequestHolder friendRequestHolder, final int i) {

        friendRequestHolder.tvFriendName.setText(fullNames.get(i));
        SharedPreferences preferences = context.getSharedPreferences(Login.PREFNAME,Context.MODE_PRIVATE);
        userId = preferences.getString(Login.USERID,"none");

        friendRequestHolder.btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateFriendRequestStatus().execute(userIds.get(i));
            }
        });

    }

    @Override
    public int getItemCount() {
        return userIds.size();
    }

    class FriendRequestHolder extends RecyclerView.ViewHolder{

        private TextView tvFriendName;
        private Button btAccept;

        public FriendRequestHolder(@NonNull View itemView) {
            super(itemView);

            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            btAccept = itemView.findViewById(R.id.btAcceptRequest);
        }
    }

    private class UpdateFriendRequestStatus extends AsyncTask<String,String,String>{
        HttpURLConnection request;
        BufferedReader bufferedReader;
        InputStream inputStream;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {
            Uri uri = Uri.parse(acceptFriendReqUrl).buildUpon().appendQueryParameter("userid",userId).appendQueryParameter("friendid",strings[0]).build();
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
            if (s.equals("true")){
                Toast.makeText(context,"Accepted",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getDataFromJson(String jsonData){
        String res = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            res = jsonObject.getString("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }
}
