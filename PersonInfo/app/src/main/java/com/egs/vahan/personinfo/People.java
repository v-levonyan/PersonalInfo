package com.egs.vahan.personinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.egs.vahan.personinfo.database.PersonBaseHelper;
import com.egs.vahan.personinfo.database.PersonCursorWrapper;
import com.egs.vahan.personinfo.database.PersonDbSchema;
import com.egs.vahan.personinfo.database.PersonDbSchema.PersonTable;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private List<Person> mPersons;
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
        mPersons = new ArrayList<>();
        mDatabase = new PersonBaseHelper(mContext).getWritableDatabase();
    }

    public List<Person> getPersons() {
        return mPersons;
    }

    public int getCount() {
        return mPersons.size();
    }

    public Person getPerson(UUID id) {
        for (Person person : mPersons) {
            if (person.getId().equals(id)) {
                return person;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return getPersons().isEmpty();
    }


    public void setPersons(List<Person> persons) {
        mPersons = persons;
    }

    public List<Person> getPersonsFromDb() {
        List<Person> persons = new ArrayList<>();
        PersonCursorWrapper cursor = queryPersons(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Person person = cursor.getPerson();
                persons.add(person);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return persons;
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

    public void addPerson(Person p) {
        ContentValues values = getContentValues(p);
        mDatabase.insert(PersonTable.NAME, null, values);
    }

    private static ContentValues getContentValues(Person person) {
        ContentValues values = new ContentValues();
        values.put(PersonTable.Cols.UUID, person.getId().toString());
        values.put(PersonTable.Cols.NAME, person.getFirstname());
        values.put(PersonTable.Cols.USERNAME, person.getLastName());
        values.put(PersonTable.Cols.ADDRESS, person.getAdress());
        values.put(PersonTable.Cols.EMAIL, person.getEmail());
        values.put(PersonTable.Cols.PHONE, person.getPhone());

        return values;
    }


}

