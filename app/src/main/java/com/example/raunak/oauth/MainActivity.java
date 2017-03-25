package com.example.raunak.oauth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    String tweetText;
    public void logout(View v)
    {
        Intent in=new Intent(this,Login.class);
        startActivity(in);
    }
    public void tweet(View v)
    {
        Log.i("hey","hello");
        EditText t= (EditText) findViewById(R.id.twe);
        tweetText=t.getText().toString();
        Stat s=new Stat();
        s.execute();



    }
    twitter4j.Status status;
    class Stat extends AsyncTask<Void, Void, Void>
    {


        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                    "oauth", Context.MODE_PRIVATE);




            ConfigurationBuilder cb = new ConfigurationBuilder();
            String consumerKey= sharedPref.getString("consumerKey","");
            String accessToken=sharedPref.getString("accessToken","");
            String consumerSecret=sharedPref.getString("consumerSecret","");
            String accessSecret=sharedPref.getString("accessSecret","");




            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("oCMwMQQcySr05kQgGUZjGo576")
                    .setOAuthConsumerSecret("tP3O2bjNCc6qE7p1T6KK3cWQSgTyUBuafiPKwOw6P4DaSOcIHU")
                    .setOAuthAccessToken("773211879866937344-RXHvvaMAAYmxN93DDT11Ngc4wJfE7lU")
                    .setOAuthAccessTokenSecret("ilCXkCCS3LYEDAJ23qWrdMP3kbwESRg2rJVkJwsbSSYq3");

            TwitterFactory factory = new TwitterFactory(cb.build());
            Twitter twitter = factory.getInstance();
            try {
                System.out.println(twitter.getScreenName());

                 status = twitter.updateStatus(tweetText);
            }
            catch (Exception e)
            {
                Log.i("hey",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MainActivity.this,"Successfully updated the status to [" + status.getText() + "].",Toast.LENGTH_LONG).show();

        }
    }
}
