package com.dj.zerionmanageapp;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> ids;
    public static ArrayList<DetailsModel> items;
    ZerionAPI zerionAPI = new ZerionAPI();
    ZerionAPI serviceDetails;
    String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.list_view);
        items = new ArrayList<>();

        Intent intent = getIntent();
        if (intent.hasExtra("access_token")) {
            access_token = intent.getStringExtra("access_token");
            zerionAPI.generateIdURL(access_token);
            new generateData().execute();
        }
    }

    private class generateData extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params) {
            zerionAPI.getResponse();
            ids = zerionAPI.getIdFromResponse();
            for (int i = 0; i < ids.size(); i++) {
                serviceDetails = new ZerionAPI();
                serviceDetails.generateDetailURL(access_token, i);
                serviceDetails.getResponse();
                DetailsModel detailsModel = serviceDetails.getItemDetailFromResponse();
                items.add(detailsModel);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ListActivity.this, null, "Loading, Please Wait...");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            arrayAdapter = new ArrayAdapter<>(ListActivity.this, android.R.layout.simple_list_item_1, ids);
            listView.setAdapter(arrayAdapter);
            progressDialog.dismiss();

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