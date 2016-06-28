package com.egs.vahan.personinfo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.egs.vahan.personinfo.database.PersonDbSchema.PersonTable;

/**
 * Created by vahanl on 6/24/16.
 */
public class PersonBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "personBASE.db";

    public PersonBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PersonTable.NAME + "(" +
                PersonTable.Cols.UUID + ", " +
                PersonTable.Cols.NAME + ", " +
                PersonTable.Cols.USERNAME + ", " +
                PersonTable.Cols.EMAIL + ", " +
                PersonTable.Cols.ADDRESS + ", " +
                PersonTable.Cols.PHONE + ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
