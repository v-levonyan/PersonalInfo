package com.egs.vahan.personinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vahan on 6/19/16.
 */
public class PersonListFragment extends Fragment {

    private RecyclerView mPersonRecyclerView;
    private PersonAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);

        mPersonRecyclerView = (RecyclerView) view.findViewById(R.id.person_recycler_view);
        mPersonRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        People people = People.get(getActivity());
        List<Person> persons = people.getPersons();

        mAdapter = new PersonAdapter(persons);
        mPersonRecyclerView.setAdapter(mAdapter);
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
}
