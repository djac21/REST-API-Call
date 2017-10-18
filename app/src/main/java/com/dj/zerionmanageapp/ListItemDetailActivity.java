package com.dj.zerionmanageapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class ListItemDetailActivity extends AppCompatActivity {
    String nameString, ageString, numberString, dateString, imageString;
    TextView detailName, detailAge, detailNumber, detailDate;
    ImageView detailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nameString = getIntent().getStringExtra("name");
        ageString = getIntent().getStringExtra("age");
        numberString = getIntent().getStringExtra("number");
        dateString = getIntent().getStringExtra("date");
        imageString = getIntent().getStringExtra("image");

        detailName = (TextView) findViewById(R.id.detail_name);
        detailAge = (TextView) findViewById(R.id.detail_age);
        detailNumber = (TextView) findViewById(R.id.detail_number);
        detailDate = (TextView) findViewById(R.id.detail_date);
        detailImage = (ImageView) findViewById(R.id.detail_image);

        detailName.setText("Name: " + nameString);
        detailAge.setText("Age: " + ageString);
        detailNumber.setText("Number: " + numberString);
        detailDate.setText("Date: " + dateString);

        new getItemImage().execute();
    }

    private class getItemImage extends AsyncTask<Void, Void, Void> {
        Bitmap image;
        URL url = null;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                url = new URL(imageString);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            detailImage.setImageBitmap(image);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
