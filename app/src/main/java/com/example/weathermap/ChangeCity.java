package com.example.weathermap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChangeCity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);

        ImageButton imageButton = (ImageButton) findViewById(R.id.left_button);
        final EditText editText = (EditText) findViewById(R.id.editText);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeCity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String newCity = editText.getText().toString();
                Intent backPage = new Intent(ChangeCity.this, MainActivity.class);
                backPage.putExtra("City", newCity);
                startActivity(backPage);
                return false;
            }
        });
    }
}
