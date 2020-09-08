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
import android.widget.TextView;

import com.xiaohao.studymanager.R;
import com.xiaohao.studymanager.dao.StudyManagerDao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AddStudyHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_study_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final SQLiteDatabase database = StudyManagerDao.getSQLiteDatabase(getApplicationContext());
        final Intent intent = getIntent();

        final int id = intent.getIntExtra("id", 0);


        Cursor cursor = database
                .query("STUDY_HISTORY", null, "id=?", new String[]{Integer.toString(id)}, null, null, null);
        while (cursor.moveToNext()) {
            TextView content = findViewById(R.id.content);
            content.setText(cursor.getString(2));
        }
        cursor.close();

        Button submit = findViewById(R.id.study_history_submit);

        final EditText content = findViewById(R.id.content);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("content", content.getText().toString());
                if (id != 0) {
                    database.update("STUDY_HISTORY", values, "id=?", new String[]{String.valueOf(id)});
                } else {
                    values.put("create_date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    final int planId = intent.getIntExtra("plan_id", 0);
                    values.put("plain_id", planId);
                    database.insert("STUDY_HISTORY", null, values);
                }
                finish();
            }
        });
    }
}