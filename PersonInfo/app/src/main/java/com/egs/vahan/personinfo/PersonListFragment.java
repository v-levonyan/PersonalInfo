package com.egs.vahan.personinfo;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;
    private LinearLayout mProgressBarLayout;
    private boolean isConnected = false;

    private boolean connectToNetwork() {
        String stringUrl = "http://jsonplaceholder.typicode.com/users";

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new RetrieveUserInfoFromNet().execute(stringUrl);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        isConnected = connectToNetwork();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);

        mPersonRecyclerView = (RecyclerView) view.findViewById(R.id.person_recycler_view);
        mPersonRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mErrorTextView = (TextView) container.findViewById(R.id.error_text_id);
        mProgressBar = (ProgressBar) container.findViewById(R.id.progress_bar_id);
        mProgressBarLayout = (LinearLayout) container.findViewById(R.id.progress_bar_layout);

        updateUI();
        if (!isConnected && People.get(getActivity()).isEmpty()) {
            mErrorTextView.setVisibility(View.VISIBLE);
        }

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.person_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_reload:
                connectToNetwork();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Private classes

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

        public void setPersons(List<Person> persons) {
            mPersons = persons;
        }
    }

    private class RetrieveUserInfoFromNet extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            try {
                 fetchItems(doGetRequest(strings[0]));
            } catch (IOException e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mProgressBarLayout.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressBarLayout.setVisibility(View.INVISIBLE);

            mErrorTextView.setVisibility(View.INVISIBLE);
            updateUI();
        }

        private void fetchItems(String jsonString) {
            People people = People.get(getActivity());
            try {
                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.optInt("id");
                    String name = jsonObject.optString("name").toString();
                    String lastName = jsonObject.optString("username").toString();
                    String address = jsonObject.optString("address").toString();
                    String email = jsonObject.optString("email").toString();
                    String phone = jsonObject.optString("phone").toString();
                    Person person = new Person();
                    person.setId(id);
                    person.setFirstname(name);
                    person.setLastName(lastName);
                    person.setAdress(address);
                    person.setEmail(email);
                    person.setPhone(phone);
                    if (people.getPersonFromDb(id) == null) {
                        people.addPerson(person);
                    } else {
                        people.updatePerson(person);
                    }
                    publishProgress((int) ((i / (float) jsonArray.length()) * 100));

                    if (isCancelled()) {
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    //Private methods

    private String doGetRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        Log.d("response: ", response.toString());
        return response.body().string();
    }

    private void updateUI() {
        if (isAdded()) {
            List<Person> persons = People.get(getActivity()).getPersonsFromDb();
            mAdapter = new PersonAdapter(persons);
            mPersonRecyclerView.setAdapter(mAdapter);
            updateSubtitle();
        }
    }

    private void updateSubtitle() {
        int personCount = People.get(getActivity()).getCount();
        String subtitle = getString(R.string.subtitle_format, personCount);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
}
