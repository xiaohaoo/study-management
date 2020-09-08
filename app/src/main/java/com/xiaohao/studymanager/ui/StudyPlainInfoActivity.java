package com.xiaohao.studymanager.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaohao.studymanager.R;
import com.xiaohao.studymanager.dao.StudyManagerDao;

public class StudyPlainInfoActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_plain_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        this.initButton();
        database = StudyManagerDao.getSQLiteDatabase(getApplicationContext());

    }


    public void initData() {

        Cursor cursor = database
                .query("STUDY_MANAGER", null, "id=?", new String[]{Integer.toString(id)}, null, null, null);
        while (cursor.moveToNext()) {
            TextView title = findViewById(R.id.title);
            TextView content = findViewById(R.id.content);
            TextView process = findViewById(R.id.process);
            TextView endDate = findViewById(R.id.end_date);
            title.setText(cursor.getString(1));
            content.setText(cursor.getString(2));
            process.setText(String.format("当前进度：%s%%", cursor.getInt(3)));
            endDate.setText(String.format("截止时间：%s", cursor.getString(4)));
        }
        cursor.close();


        LinearLayout linearLayout = findViewById(R.id.study_history);
        linearLayout.removeAllViews();

        Cursor studyHistoryCursor = database
                .query("STUDY_HISTORY", null, "plain_id=?", new String[]{Integer.toString(id)}, null, null, null);

        while (studyHistoryCursor.moveToNext()) {
            View item = StudyPlainInfoActivity.this.getLayoutInflater().inflate(R.layout.study_history_item, null);
            TextView content = item.findViewById(R.id.study_history_content);
            content.setText(studyHistoryCursor.getString(2));
            TextView createDate = item.findViewById(R.id.create_date);
            createDate.setText(studyHistoryCursor.getString(4));

            ImageView img = item.findViewById(R.id.study_history_img);
            TextView edit = item.findViewById(R.id.study_history_edit);
            TextView delete = item.findViewById(R.id.study_history_delete);
            final int id = studyHistoryCursor.getInt(0);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudyPlainInfoActivity.this,AddStudyHistoryActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.delete("STUDY_HISTORY", "id=?", new String[]{String.valueOf(id)});
                    Toast.makeText(StudyPlainInfoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    StudyPlainInfoActivity.this.initData();
                }
            });
            linearLayout.addView(item);

        }
        studyHistoryCursor.close();


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.initData();

    }

    private void initButton() {
        Button edit = findViewById(R.id.stydy_plain_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudyPlainInfoActivity.this, AddStudyPlainActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

        Button delete = findViewById(R.id.stydy_plain_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("删除id=====>", String.valueOf(id));
                int study_manager = database.delete("STUDY_MANAGER", "id=?", new String[]{String.valueOf(id)});
                Log.i("删除=====>", String.valueOf(study_manager));
                finish();
            }
        });


        Button historyAdd = findViewById(R.id.study_history_add);
        historyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyPlainInfoActivity.this, AddStudyHistoryActivity.class);
                intent.putExtra("plan_id", id);
                startActivity(intent);
            }
        });

    }
}

