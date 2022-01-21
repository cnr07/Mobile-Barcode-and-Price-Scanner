package com.cnr.pricerunner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void scanBarcode(View view) {
        Intent intent = new Intent(MainActivity.this,ScanBarcode.class);
        //String w="caca";
        //intent.putExtra("w",w);
        startActivity(intent);
    }

    public void about(View view) {
        Toast.makeText(getApplicationContext(),"Can Erdoğan 20170702044"+System.lineSeparator()+"can.erdogan@std.yeditepe.edu.tr",Toast.LENGTH_LONG).show();
    }
}