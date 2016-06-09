package com.example.check24.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.check24.R;

/**
 * Created by marcingawel on 09.06.2016.
 */

public class ChoiceActivity extends AppCompatActivity {

    public static final String EXTRA_CHOICE = "com.example.check.choice";
    public static final int CHOICE_SUM = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        Button sum = (Button) findViewById(R.id.button_sum);
        sum.setOnClickListener((v) -> {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_CHOICE, CHOICE_SUM);
            setResult(Activity.RESULT_OK);
            finish();
        });
    }
}
