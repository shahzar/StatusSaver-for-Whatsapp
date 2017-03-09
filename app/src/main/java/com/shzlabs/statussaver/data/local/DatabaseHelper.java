package com.shzlabs.statussaver.data.local;

import android.database.Cursor;
import android.util.Log;

import com.shzlabs.statussaver.data.model.SampleModel;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DatabaseHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        SqlBrite.Builder builder = new SqlBrite.Builder();
        mDb = builder.build().wrapDatabaseHelper(dbOpenHelper, Schedulers.immediate());
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<List<SampleModel>> getDeeds() {
        ArrayList<String> tables = new ArrayList<>();
        tables.add(Db.TheSampleTable.TABLE_NAME);

        String query = "SELECT * FROM " + Db.TheSampleTable.TABLE_NAME;

        return mDb.createQuery(tables, query)
                .mapToList(new Func1<Cursor, SampleModel>() {
                    @Override
                    public SampleModel call(Cursor cursor) {
                        String name = cursor.getString(cursor.getColumnIndex(Db.TheSampleTable.COLUMN_NAME));
                        String description = cursor.getString(cursor.getColumnIndex(Db.TheSampleTable.COLUMN_DESCRIPTION));
                        return new SampleModel(name, description);
                    }
                });
    }

    public void additem(SampleModel item) {
        BriteDatabase.Transaction transaction = mDb.newTransaction();

        try {
            long result = mDb.insert(Db.TheSampleTable.TABLE_NAME,
                    Db.TheSampleTable.toContentValues(item));
            if (result <0) Log.e(TAG, "addItem: Error inserting to table " + Db.TheSampleTable.TABLE_NAME);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }
}