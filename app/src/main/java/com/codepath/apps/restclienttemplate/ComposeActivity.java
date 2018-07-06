package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    private final int REQUEST_CODE = 20;
    EditText editText;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);
    }

    public void tweetIt(View v){
        editText = findViewById(R.id.et_simple);
        String message = editText.getText().toString();

        client.sendTweet(message, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess ( int statusCode, Header[] headers, JSONObject response){
                super.onSuccess(statusCode, headers, response);
                Log.d("SendTweetResponseCallbk", "successfully sent tweet!");
                //construct a new tweet
                try {
                    tweet = Tweet.fromJSON(response);

                    //send back the new tweet as a result to parent activity
                    Intent i = new Intent();
                    i.putExtra("mode", Parcels.wrap(tweet));          //this wraps the tweet
                    setResult(RESULT_OK, i);
                    finish();             //this function already knows where to go (bc ..forResult), so just need finish

                    //notify the adapter that a new tweet has been inserted


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure (int statusCode, Header[] headers, Throwable throwable, JSONObject
            errorResponse){
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("SendTweetResponseCallbk", "failed to send a tweet!");
                throwable.printStackTrace();
            }

            @Override
            public void onFailure ( int statusCode, Header[] headers, String responseString, Throwable
            throwable){
                super.onFailure(statusCode, headers, responseString, throwable);

                Log.d("SendTweetResponseCallbk", "failed to send a tweet!");
                throwable.printStackTrace();
            }
        });

        //start new intent to go back to original activity, because we are done tweeting (onClick)

    }

}
