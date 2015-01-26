package com.example.magdi.instagramviewer;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InstagramActivity extends ActionBarActivity {

    public static final String CLENT_ID = "d874b9d1248c4ab0b60d39bc7c928a21";
    private ArrayList<InstagramPhotos> photos;
    private InstagramPhotoAdapter aphotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                //swipeContainer.setRefreshing(true);
                fetchPopularPhotos();

            }

        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<InstagramPhotos>();
        //create adapter bind to the data in arraylist
        aphotos = new InstagramPhotoAdapter(this, photos);
        //pouplate the data into listview
        ListView lvphotos = (ListView) findViewById(R.id.lvphotos);
        //set adapter to listview
        lvphotos.setAdapter(aphotos);
        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(popularUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("INFO", response.toString());
                //url,username capture height

                JSONArray photoJSON = null;
                try {
                    photos.clear();
                    photoJSON = response.getJSONArray("data");
                    for (int i = 0; i < photoJSON.length(); i++) {//get each photo object
                        JSONObject photoObject = photoJSON.getJSONObject(i);
                        InstagramPhotos photo = new InstagramPhotos();
                        photo.username = photoObject.getJSONObject("user").getString("username");

                        if(photoObject.getJSONObject("user") != null) {
                            photo.profImage = photoObject.getJSONObject("user").getString("profile_picture");
                            Log.i("INFO", "profImage"+ photo.profImage.toString());
                        }

                        if(photoObject.getJSONObject("caption") != null) {
                            photo.caption = photoObject.getJSONObject("caption").getString("text");
                        }
                        if(photoObject.getJSONObject("comments") != null) {

                            JSONArray user  = photoObject.getJSONObject("comments").getJSONArray("data");
                            for (int k = 0; k <= 2; k++) {//get each photo object
                                JSONObject userbject = user.getJSONObject(k);
                                photo.comments = userbject.getString("text");


                            }
                            Log.i("INFO", "comments:" + photo.comments);
                        }
                        photo.imageurl = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageheight = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        if(photoObject.getJSONObject("likes") != null) {
                            photo.likescount = photoObject.getJSONObject("likes").getInt("count");

                        }
                        swipeContainer.setRefreshing(false);
                        photos.add(photo);

                    }
                    aphotos.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instagram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
