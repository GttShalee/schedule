package com.example.schedule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    public final String DB_NAME = "classes_db.db";
    public final String TABLE_NAME = "classes_db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_main2);

        ImageButton btnImage = findViewById(R.id.page);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("我点击了图片按钮。");
            }
        });

        //拿到数据库对象，为了读取数据
        DBHelper dbHelper = new DBHelper(MainActivity.this, DB_NAME, null, 1);

        //动态渲染课程框
        framework();

        //将数据库课程渲染到课程框
        applyDraw(dbHelper);

    }

    public GridLayout LayoutColumn(int i) {

        GridLayout gridLayout = findViewById(R.id.d1);

        switch (i) {
            case 1: {
                gridLayout = findViewById(R.id.d1);
                break;
            }
            case 2: {
                gridLayout = findViewById(R.id.d2);
                break;
            }
            case 3: {
                gridLayout = findViewById(R.id.d3);
                break;
            }
            case 4: {
                gridLayout = findViewById(R.id.d4);
                break;
            }
            case 5: {
                gridLayout = findViewById(R.id.d5);
                break;
            }
            case 6: {
                gridLayout = findViewById(R.id.d6);
                break;
            }
            case 7: {
                gridLayout = findViewById(R.id.d7);
                break;
            }
        }
        return gridLayout;
    }

    public void framework() {

        GridLayout gridLayout;
        int id = 1;

        for (int i = 1; i < 8; i++) {

            gridLayout = LayoutColumn(i);

            for (int j = 1; j < 10; j +=2) {
                TextView textView1 = new TextView(this);

                textView1.setId(id++);
                textView1.setText("");
                textView1.setMaxLines(5);
                textView1.setEllipsize(TextUtils.TruncateAt.END);
                textView1.setBackgroundColor(Color.parseColor("#F0FFFF"));
                textView1.setGravity(Gravity.CENTER);


                GridLayout.LayoutParams params1 = new GridLayout.LayoutParams();
                params1.rowSpec = GridLayout.spec( j, 2,1);
                params1.setMargins(5,10,5,10);
                params1.width = GridLayout.LayoutParams.MATCH_PARENT;
                params1.height = 0;

                gridLayout.addView(textView1, params1);
            }

        }

    }

    public void applyDraw(DBHelper dbHelper) {

        //从数据库拿到课程数据保存在链表
        List<Classes> classes = query(dbHelper);

        for (Classes aClass : classes) {
            //第几节课
            int i = Integer.parseInt(aClass.getC_time().charAt(0) + "");

            //星期几
            int j = utils.getDay(aClass.getC_day());

            //获取此课程对应TextView的id
            TextView Class = findViewById((j - 1) * 5 + ((i - 1)/2 + 1));

            //判断如果课程星期==当前星期，如此课程信息和当前都是星期二就把背景颜色更换。
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            if (aClass.getC_day().equals(simpleDateFormat.format(date).toString())) {
                Class.setBackgroundColor(Color.rgb(28, 217, 204));
            }

            //课程表信息映射出来
            Class.setText(aClass.getC_name());

            //触碰此课程框触发
            Class.setOnTouchListener(this);

        }

    }

    @SuppressLint("Range")
    public List<Classes> query(DBHelper dbHelper) {

        List<Classes> classes = new ArrayList<>();
        // 通过DBHelper类获取一个读写的SQLiteDatabase对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 参数1：table_name
        // 参数2：columns 要查询出来的列名。相当于 select  *** from table语句中的 ***部分
        // 参数3：selection 查询条件字句，在条件子句允许使用占位符“?”表示条件值
        // 参数4：selectionArgs ：对应于 selection参数 占位符的值
        // 参数5：groupby 相当于 select *** from table where && group by ... 语句中 ... 的部分
        // 参数6：having 相当于 select *** from table where && group by ...having %%% 语句中 %%% 的部分
        // 参数7：orderBy ：相当于 select  ***from ？？  where&& group by ...having %%% order by@@语句中的@@ 部分，如： personid desc（按person 降序）
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        // 将游标移到开头
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) { // 游标只要不是在最后一行之后，就一直循环

            if (!cursor.getString(cursor.getColumnIndex("c_day")).equals("0")) {
                Classes Class = new Classes();

                Class.setC_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("c_id"))));
                Class.setC_name(cursor.getString(cursor.getColumnIndex("c_name")));
                Class.setC_time(cursor.getString(cursor.getColumnIndex("c_time")));
                Class.setC_day(cursor.getString(cursor.getColumnIndex("c_day")));

                classes.add(Class);
            }

            // 将游标移到下一行
            cursor.moveToNext();

        }

        db.close();
        return classes;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);


        MenuItem menuItem=menu.findItem(R.id.action_menu);

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DialogModal.class);
                startActivity(intent);
                return true;
            }
        });

        return  super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("Range")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN: {
                TextView textView = (TextView) view;


                DBHelper dbHelper = new DBHelper(MainActivity.this, DB_NAME, null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // 参数1：table_name
                // 参数2：columns 要查询出来的列名。相当于 select  *** from table语句中的 ***部分
                // 参数3：selection 查询条件字句，在条件子句允许使用占位符“?”表示条件值
                // 参数4：selectionArgs ：对应于 selection参数 占位符的值
                // 参数5：groupby 相当于 select *** from table where && group by ... 语句中 ... 的部分
                // 参数6：having 相当于 select *** from table where && group by ...having %%% 语句中 %%% 的部分
                // 参数7：orderBy ：相当于 select  ***from ？？  where&& group by ...having %%% order by@@语句中的@@ 部分，如： personid desc（按person 降序）
//        Cursor cursor = db.query(TABLE_NAME, null, "c_id=?", new String[]{String.valueOf(textView.getId())}, null, null, null);
                Cursor cursor = db.query(TABLE_NAME, null, "c_id=?", new String[]{String.valueOf(textView.getId())}, null, null, null);

                // 将游标移到开头
                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    Classes Class = new Classes();
                    System.out.println(textView.getId());
                    System.out.println(cursor.getString(cursor.getColumnIndex("c_name")));

                    Intent intent = new Intent();
                    intent.putExtra("name", cursor.getString(cursor.getColumnIndex("c_name")));
                    intent.putExtra("time", cursor.getString(cursor.getColumnIndex("c_time")));
                    intent.putExtra("day", cursor.getString(cursor.getColumnIndex("c_day")));
                    intent.putExtra("teacher", cursor.getString(cursor.getColumnIndex("c_teacher")));
                    intent.setClass(MainActivity.this, Detail.class);
                    startActivity(intent);

                }
                return true;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
        }

        return false;
    }

//    private View.OnClickListener {
//        Intent intent = new Intent();
//        intent.setClass(MainActivity.this,MainActivity2.class);
//        startActivity(intent);
//    }

}
