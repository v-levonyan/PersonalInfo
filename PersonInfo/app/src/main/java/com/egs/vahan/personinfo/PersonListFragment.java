package com.egs.vahan.personinfo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vahan on 6/19/16.
 */
public class PersonListFragment extends Fragment {

    private RecyclerView mPersonRecyclerView;
    private PersonAdapter mAdapter;
    private People mPeople;
    private String networkError;
    private TextView mErrorTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPeople = People.get(getActivity());
//        mPersons = new ArrayList<>();
        String stringUrl = "http://jsonplaceholder.typicode.com/users";

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new RetrieveUserInfoFromNet().execute(stringUrl);
        } else {
            networkError = "Unable to connect";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);

        mPersonRecyclerView = (RecyclerView) view.findViewById(R.id.person_recycler_view);
        mPersonRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (networkError != null) {
            mErrorTextView = (TextView) container.findViewById(R.id.error_text);
            mErrorTextView.setText(networkError);
        }

        return view;
    }

    private void updateUI() {
        if (isAdded()) {
            mAdapter = new PersonAdapter(mPeople.getPersons());
            mPersonRecyclerView.setAdapter(mAdapter);
        }
    }

    private class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Person mPerson;

        private TextView mNameTextView;
        private TextView mLastNameTextView;


        public PersonHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_person_name_text_view);
            mLastNameTextView = (TextView) itemView.findViewById(R.id.list_item_person_email_text_view);
        }

        public void bindPerson(Person person) {
            mPerson = person;
            mNameTextView.setText(mPerson.getFirstname());
            mLastNameTextView.setText(mPerson.getEmail());
        }

        @Override
        public void onClick(View view) {
            Intent intent = PersonActivity.newIntent(getActivity(), mPerson.getId());
            startActivity(intent);
        }
    }

    private class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {
        private List<Person> mPersons;

        public PersonAdapter(List<Person> persons) {
            mPersons = persons;
        }

        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_person, parent, false);

            return new PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(PersonHolder holder, int position) {
            Person person = mPersons.get(position);
            holder.bindPerson(person);
        }

        @Override
        public int getItemCount() {
            return mPersons.size();
        }
    }

    private class RetrieveUserInfoFromNet extends AsyncTask<String, Void, List<Person>> {
        @Override
        protected List<Person> doInBackground(String... strings) {
            try {
                return fetchItems(doGetRequest(strings[0]));
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Person> persons) {
            mPeople.setPersons(persons);
            updateUI();
        }

    }

    private List<Person> fetchItems(String jsonString) {
        List<Person> persons = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

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

                persons.add(person);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return persons;
    }

    private String doGetRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        Log.d("response: ", response.toString());
        return response.body().string();
    }
}
