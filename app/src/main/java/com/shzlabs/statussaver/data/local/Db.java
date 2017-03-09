package com.shzlabs.statussaver.data.local;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.shzlabs.statussaver.data.model.SampleModel;


public class Db {

    public Db() {
    }

    public abstract static class TheSampleTable implements BaseColumns {
        public static final String TABLE_NAME = "thesample";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_DESCRIPTION + " TEXT " +
                        "); ";

        public static ContentValues toContentValues(SampleModel model) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME, model.getName());
            contentValues.put(COLUMN_DESCRIPTION, model.getDescription());
            return contentValues;
        }
    }
}