package com.xiaohao.studymanager.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.xiaohao.studymanager.R;
import com.xiaohao.studymanager.dao.StudyManagerDao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddStudyPlainActivity.class);
                startActivity(intent);
            }
        });

        database = StudyManagerDao.getSQLiteDatabase(getApplicationContext());
    }

    public void initData() {
        Cursor cursor = database.query("STUDY_MANAGER", null, null, null, null, null, null);
        LinearLayout linearLayout = findViewById(R.id.study_managers);
        linearLayout.removeAllViews();
        while (cursor.moveToNext()) {
            View item = MainActivity.this.getLayoutInflater().inflate(R.layout.study_plain_item, null);
            Log.i("linearLayout", String.valueOf(item.hashCode()));
            TextView process = item.findViewById(R.id.study_plain_item_process);
            TextView title = item.findViewById(R.id.study_plain_item_title);
            TextView endDate = item.findViewById(R.id.study_plain_item_end_date);
            title.setText(cursor.getString(1));
            process.setText(String.format("当前进度：%s%%", cursor.getInt(3)));
            endDate.setText(String.format("截止时间：%s", cursor.getString(4)));
            final Integer id = cursor.getInt(0);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, StudyPlainInfoActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
            linearLayout.addView(item);
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        this.initData();
    }
}