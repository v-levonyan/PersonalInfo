package com.egs.vahan.personinfo;

import android.content.Context;
import android.util.Log;

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
        mContext = context;
        mPersons = new ArrayList<>();
        try {
            JSONObject jsonRootObject = new JSONObject(loadJSONFromAsset());
            JSONArray jsonArray = jsonRootObject.optJSONArray("Persons");

            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.optString("firstName").toString();
                String lastName = jsonObject.optString("lastName").toString();
                String address = jsonObject.optString("address").toString();
                String email = jsonObject.optString("email").toString();
                String phone = jsonObject.optString("phoneNumber").toString();
                int age = jsonObject.optInt("age");
                Person person = new Person();
                person.setFirstname(name);
                person.setLastName(lastName);
                person.setAdress(address);
                person.setEmail(email);
                person.setAge(age);
                person.setPhone(phone);
                Log.d("Person name: ", name);
                Log.d("Person lastname: ", lastName);
                Log.d("Person address: ", address);

                mPersons.add(person);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Person> getPersons() {
        return mPersons;
    }

    public Person getPerson(UUID id) {
        for (Person person : mPersons) {
            if (person.getId().equals(id)) {
                return person;
            }
        }
        return null;
    }
}
