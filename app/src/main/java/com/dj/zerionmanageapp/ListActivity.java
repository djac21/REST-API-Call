package com.dj.zerionmanageapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> ids;
    public static ArrayList<DetailsModel> items;
    API api = new API();
    API serviceDetails;
    String access_token;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = findViewById(R.id.list_view);
        progressBar = findViewById(R.id.progressBar);
        items = new ArrayList<>();

        Intent intent = getIntent();
        if (intent.hasExtra("access_token")) {
            access_token = intent.getStringExtra("access_token");
            api.generateIdURL(access_token);
            new generateData().execute();
        }
    }

    private class generateData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            api.getResponse();
            ids = api.getIdFromResponse();
            for (int i = 0; i < ids.size(); i++) {
                serviceDetails = new API();
                serviceDetails.generateDetailURL(access_token, i);
                serviceDetails.getResponse();
                DetailsModel detailsModel = serviceDetails.getItemDetailFromResponse();
                items.add(detailsModel);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            arrayAdapter = new ArrayAdapter<>(ListActivity.this, android.R.layout.simple_list_item_1, ids);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String itemName = items.get(position).getName();
                    String itemAge = items.get(position).getAge();
                    String itemNumber = items.get(position).getPhone();
                    String itemDate = items.get(position).getDate();
                    String itemImage = items.get(position).getImage();

                    Intent intent = new Intent(ListActivity.this, ListItemDetailActivity.class);
                    intent.putExtra("name", itemName);
                    intent.putExtra("age", itemAge);
                    intent.putExtra("number", itemNumber);
                    intent.putExtra("date", itemDate);
                    intent.putExtra("image", itemImage);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout)
            finish();

        return super.onOptionsItemSelected(item);
    }
}