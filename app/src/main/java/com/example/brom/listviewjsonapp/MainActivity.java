package com.example.brom.listviewjsonapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayAdapter adapter;
    private List<Mountain> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchData().execute();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MountainAdapter(listData, new MountainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Mountain item) {
                Toast.makeText(getApplicationContext(),"HLOO",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Details.class);
                intent.putExtra("image", item.imgInfo());
                intent.putExtra("info", item.info());
                intent.putExtra("name", item.toString());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);


    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(getApplicationContext(), "This is an app about mountains" + '\n' + "Created by Alice Anglesjö",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.refresh:
                adapter.clear();
                new FetchData().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class FetchData extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            // These two variables need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a Java string.
            String jsonStr = null;

            try {
                // Construct the URL for the Internet service
                URL url = new URL("http://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");

                // Create the request to the PHP-service, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
                return jsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in
                // attempting to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Network error", "Error closing stream", e);
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(String o) {

            super.onPostExecute(o);
            Log.d("alicehej", "data" + o);
            // This code executes after we have received our data. The String object o holds
            // the un-parsed JSON string or is null if we had an IOException during the fetch.

            // Implement a parsing code that loops through the entire JSON and creates objects
            // of our newly created Mountain class.
            try {
                mRecyclerView.setAdapter(null);
                listData.clear();
                // Ditt JSON-objekt som Java
                JSONArray json1 = new JSONArray(o);

                for(int i = 0; i < json1.length(); i++) {

                    JSONObject berg = json1.getJSONObject(i);
                    Log.d("alicehej", "berg" + berg.toString());

                    int ID = berg.getInt("ID");
                    String name = berg.getString("name");
                    String type = berg.getString("type");
                    String company = berg.getString("company");
                    String location = berg.getString("location");
                    String category = berg.getString("category");
                    int size = berg.getInt("size");
                    int cost = berg.getInt("cost");
                    JSONObject aData = new JSONObject(berg.getString("auxdata"));
                    String bild = aData.getString("img");


                    Mountain m = new Mountain(name, location, size, bild);

                    listData.add(m);
                    mRecyclerView.setAdapter(new MountainAdapter(listData, new MountainAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Mountain item) {
                            Intent intent = new Intent(getApplicationContext(),Details.class);
                            intent.putExtra("image", item.imgInfo());
                            intent.putExtra("info", item.info());
                            intent.putExtra("name", item.toString());
                            startActivity(intent);
                        }
                    }));

                }


            } catch (JSONException e) {
                Log.e("brom","E:"+e.getMessage());
            }



        }
    }
}

