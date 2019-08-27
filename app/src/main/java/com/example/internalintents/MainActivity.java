package com.example.internalintents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //View == button
    public void onClickWebPageBtn(View view){
        Toast.makeText(this, "Opening vk", Toast.LENGTH_SHORT).show();
        openWebPage("https://vk.com/the_old_one");
    }

    public void onClickMapBtn(View view){
        Toast.makeText(this, "Opening map", Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse("geo:0,0")
                .buildUpon()
                .appendQueryParameter("q", "Каширская улица, 24").build();

        showMap(uri);
    }

    public void onClickMyRealizationBtn(View view){}


    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        //if there is chrome or another
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        //If there is google maps
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
