package com.example.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class Detail extends Activity {

    TextView time;
    TextView clsNum;
    TextView sub;
    TextView teacher;
    TextView position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_press);

        Intent intent = getIntent();

        sub = findViewById(R.id.sub);
        sub.setText(intent.getStringExtra("name"));

        time = findViewById(R.id.time);
        time.setText(intent.getStringExtra("day"));

        clsNum = findViewById(R.id.clsNum);
        clsNum.setText(intent.getStringExtra("time"));

        teacher = findViewById(R.id.teacher);
        teacher.setText(intent.getStringExtra("teacher"));

        position = findViewById(R.id.position);
        position.setText(intent.getStringExtra("position"));

        setFinishOnTouchOutside(true);

    }

}
