package com.example.raunak.oauth;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class Twit extends Activity {
// Constants
    /**
     * Register your here app https://dev.twitter.com/apps/new and get your
     * consumer key and secret
     * */
    static String TWITTER_CONSUMER_KEY = "oCMwMQQcySr05kQgGUZjGo576"; // place your
    // cosumer
    // key here
    static String TWITTER_CONSUMER_SECRET = "tP3O2bjNCc6qE7p1T6KK3cWQSgTyUBuafiPKwOw6P4DaSOcIHU"; // place
    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    static final String TWITTER_CALLBACK_URL = "http://com.hey";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    WebView myWebView;

    // Twitter
    private static twitter4j.Twitter twitter;
    private static RequestToken requestToken;
    private AccessToken accessToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        pd = ProgressDialog.show(this, "", "loading", true);

        // All UI elements



        myWebView = (WebView) findViewById(R.id.webView2);

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url != null && url.startsWith(TWITTER_CALLBACK_URL))
                    new AfterLoginTask().execute(url);
                else {
                    Log.i("hello","rrr");
                    webView.loadUrl(url);
                }
                return true;
            }
        });



        LoginT();
    }




    public void LoginT()
    {
        new LoginTask().execute();
    }



    private void loginToTwitter() {


            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                requestToken = twitter
                        .getOAuthRequestToken(TWITTER_CALLBACK_URL);
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }








    public void handleTwitterCallback(String url) {

        Uri uri = Uri.parse(url);

        // oAuth verifier
        final String verifier = uri
                .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

        try {

            // Get the access token
            Twit.this.accessToken = twitter.getOAuthAccessToken(
                    requestToken, verifier);



            Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

            // Access Token
            String access_token = mSharedPreferences.getString(
                    PREF_KEY_OAUTH_TOKEN, "");
            // Access Token Secret
            String access_token_secret = mSharedPreferences.getString(
                    PREF_KEY_OAUTH_SECRET, "");

            AccessToken accessToken = new AccessToken(access_token,
                    access_token_secret);
            Log.i("heyacc",access_token+"  hello " +access_token_secret);

            twitter4j.Twitter twitter = new TwitterFactory(builder.build())
                    .getInstance(accessToken);

            SharedPreferences sharedPref = this.getSharedPreferences(
                    "oauth", Context.MODE_PRIVATE);

            Editor editor = sharedPref.edit();
            editor.putString("consumerKey",TWITTER_CONSUMER_KEY);
            editor.putString("consumerSecret", TWITTER_CONSUMER_SECRET);
            editor.putString("accessToken", access_token);
            editor.putString("accessSecret",access_token_secret);

            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class LoginTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub
            loginToTwitter();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub

            myWebView.loadUrl(requestToken.getAuthenticationURL());


        }

    }

    class AfterLoginTask extends AsyncTask<String, Void, Boolean> {

        protected void onPreExecute() {
            pd = ProgressDialog.show(Twit.this, "", "", true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            handleTwitterCallback(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            Intent in=new Intent(Twit.this,MainActivity.class);
            startActivity(in);

        }

    }


}

