package com.xiaohao.studymanager.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudyManagerDao extends SQLiteOpenHelper {
    private static final int VERSION = 3;
    private static final String DATABASE_NAME = "studu_plan_manager.db";
    private static StudyManagerDao studyManagerDao;

    private StudyManagerDao(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static SQLiteDatabase getSQLiteDatabase(Context context) {
        if (studyManagerDao == null) {
            studyManagerDao = new StudyManagerDao(context);
        }
        return studyManagerDao.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE STUDY_MANAGER (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "title VARCHAR,\n" +
                "content VARCHAR,\n" +
                "process INTEGER,\n" +
                "end_date TIMESTAMP\n" +
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE STUDY_HISTORY(\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "plain_id INTEGER,\n" +
                "content VARCHAR,\n" +
                "img VARCHAR,\n" +
                "create_date TIMESTAMP\n" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE  IF EXISTS STUDY_MANAGER");
        sqLiteDatabase.execSQL("DROP TABLE  IF EXISTS STUDY_MANAGER");
        onCreate(sqLiteDatabase);
    }
}
