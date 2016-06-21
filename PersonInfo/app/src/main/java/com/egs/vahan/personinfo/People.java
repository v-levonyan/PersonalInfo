package com.egs.vahan.personinfo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

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

    private String doGetRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        Log.d("response: ", response.toString());
        return response.body().string();
    }

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

        String stringUrl = "http://jsonplaceholder.typicode.com/users";

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            // display error
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

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                return doGetRequest(strings[0]);
            } catch (IOException e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.optString("name").toString();
                    String lastName = jsonObject.optString("username").toString();
                    String address = jsonObject.optString("address").toString();
                    String email = jsonObject.optString("email").toString();
                    String phone = jsonObject.optString("phone").toString();
                    int age = 50;
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

    }
}

