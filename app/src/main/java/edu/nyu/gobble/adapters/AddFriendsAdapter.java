package edu.nyu.gobble.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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

import edu.nyu.gobble.AddFriends;
import edu.nyu.gobble.R;
import edu.nyu.gobble.config.Config;
import edu.nyu.gobble.registration.Login;

public class AddFriendsAdapter extends RecyclerView.Adapter<AddFriendsAdapter.AddFriendsHolder> {

    private Context context;
    private ArrayList<String> userIds,fullNames;
    private static final String sendFriendRequestUrl = Config.baseUrl + "sendFriendRequest";
    private String userId;

    public AddFriendsAdapter(Context context, ArrayList<String> userIds, ArrayList<String> fullNames) {
        this.context = context;
        this.userIds = userIds;
        this.fullNames = fullNames;
    }

    @NonNull
    @Override
    public AddFriendsAdapter.AddFriendsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.card_friends,viewGroup,false);
        AddFriendsHolder addFriendsHolder = new AddFriendsHolder(v);

        return addFriendsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddFriendsAdapter.AddFriendsHolder addFriendsHolder, final int i) {

        addFriendsHolder.tvUserName.setText(fullNames.get(i));

        SharedPreferences preferences = context.getSharedPreferences(Login.PREFNAME,Context.MODE_PRIVATE);
        userId = preferences.getString(Login.USERID,"none");

        addFriendsHolder.btAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendFriendRequest().execute(userIds.get(i));
            }
        });

    }

    @Override
    public int getItemCount() {
        return userIds.size();
    }

    class AddFriendsHolder extends RecyclerView.ViewHolder{

        private TextView tvUserName;
        private Button btAddFriend;

        public AddFriendsHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btAddFriend = itemView.findViewById(R.id.btAddFriend);
        }
    }

    private class SendFriendRequest extends AsyncTask<String,String,String>{

        HttpURLConnection request;
        BufferedWriter bufferedWriter;
        OutputStream outputStream;
        BufferedReader bufferedReader;
        InputStream inputStream;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(sendFriendRequestUrl);
                request = (HttpURLConnection) url.openConnection();
                request.setRequestMethod("POST");
                request.setDoInput(true);
                request.setDoOutput(true);

                outputStream = request.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("user1id","UTF-8")+"="+URLEncoder.encode(userId,"UTF-8")+"&"+URLEncoder.encode("user2id","UTF-8")+"="+URLEncoder.encode(strings[0],"UTF-8");

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

            if (s.equals("true")){
                Toast.makeText(context,"Friend Request sent successfully",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"Not Sent",Toast.LENGTH_SHORT).show();
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
