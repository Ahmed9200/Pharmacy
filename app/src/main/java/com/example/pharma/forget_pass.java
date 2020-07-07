package com.example.pharma;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class forget_pass extends AppCompatActivity {


    CircularProgressButton circularProgressButton;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pass);
        circularProgressButton = findViewById(R.id.circular_btn);
        editText = findViewById(R.id.auto_text);

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText.getText().toString() != "Ahmad") {
                    circularProgressButton.setProgress(30);

                    circularProgressButton.resetProgress();
                }else {
                    circularProgressButton.setProgress(100);
                }

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (autoCompleteTextView.getText().toString() != "Ahmad") {
//                            circularProgressButton.setProgress(30);
//                            circularProgressButton.startAnimation();
//                        }
//                        circularProgressButton.setProgress(100);
//
//
//                    }
//                }, 3000);

            }
        });
    }
}