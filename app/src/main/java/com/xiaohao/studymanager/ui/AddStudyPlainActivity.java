package com.xiaohao.studymanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaohao.studymanager.R;
import com.xiaohao.studymanager.dao.StudyManagerDao;


public class AddStudyPlainActivity extends AppCompatActivity {
    private static SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_study_plan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText title = findViewById(R.id.title);
        final EditText content = findViewById(R.id.content);
        final EditText endDate = findViewById(R.id.end_date);
        final EditText process = findViewById(R.id.process);


        Intent intent = getIntent();
        final int id = intent.getIntExtra("id",0);

        database = StudyManagerDao.getSQLiteDatabase(getApplicationContext());

        if (id!=0){
            Cursor cursor = StudyManagerDao.getSQLiteDatabase(getApplicationContext())
                    .query("STUDY_MANAGER", null, "id=?", new String[]{Integer.toString(id)}, null, null, null);
            while (cursor.moveToNext()) {
                title.setText(cursor.getString(1));
                content.setText(cursor.getString(2));
                process.setText(String.valueOf(cursor.getInt(3)));
                endDate.setText(cursor.getString(4));
            }
            cursor.close();
        }

        Button submit = findViewById(R.id.study_plain_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("title", title.getText().toString());
                values.put("content", content.getText().toString());
                values.put("end_date", endDate.getText().toString());
                values.put("process", process.getText().toString());
                if (id!=0){
                    database.update("STUDY_MANAGER", values, "id=?",new String[]{String.valueOf(id)});
                }else {
                    database.insert("STUDY_MANAGER", null, values);
                }

                finish();
            }
        });

    }
}