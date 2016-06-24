package com.egs.vahan.personinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.egs.vahan.personinfo.database.PersonBaseHelper;
import com.egs.vahan.personinfo.database.PersonCursorWrapper;
import com.egs.vahan.personinfo.database.PersonDbSchema.PersonTable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by vahan on 6/20/16.
 */
public class People {

    private static People sPeople;
    private Context mContext;

    private SQLiteDatabase mDatabase;


    public static People get(Context context) {
        if (sPeople == null) {
            sPeople = new People(context);
        }
        return sPeople;
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("person_details.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private People(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PersonBaseHelper(mContext).getWritableDatabase();
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList<>();
        PersonCursorWrapper cursor = queryPersons(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                persons.add(cursor.getPerson());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return persons;
    }

    public boolean isEmpty() {
        return getPersons().isEmpty();
    }

    public Person getPerson(UUID id) {
        PersonCursorWrapper cursor = queryPersons(PersonTable.Cols.UUID + " =?",
                new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getPerson();
        } finally {
            cursor.close();
        }
    }

    public void setPersons(List<Person> persons) {

    }

    private static ContentValues getContentValues(Person person) {
        ContentValues values = new ContentValues();
        values.put(PersonTable.Cols.UUID, person.getId().toString());
        values.put(PersonTable.Cols.NAME, person.getName());
        values.put(PersonTable.Cols.USERNAME, person.getUserName());
        values.put(PersonTable.Cols.ADDRESS, person.getAdress());
        values.put(PersonTable.Cols.EMAIL, person.getEmail());
        values.put(PersonTable.Cols.PHONE, person.getPhone());

        return values;
    }

    private void addPerson(Person p) {
        ContentValues values = getContentValues(p);
        mDatabase.insert(PersonTable.NAME, null, values);
    }

    public void loadPersonsToDb(List<Person> persons) {
        for (Person person: persons) {
            addPerson(person);
        }
    }

    private PersonCursorWrapper queryPersons(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PersonTable.NAME,
                null,
                whereClause,
                whereArgs,
                null, null, null
        );
        return new PersonCursorWrapper(cursor);
    }
}

