package com.egs.vahan.personinfo;

import java.util.UUID;

/**
 * Created by vahan on 6/20/16.
 */
public class Person {

    private UUID mId;
    private String mFirstname;
    private String mLastName;
    private int mAge;
    private String mAdress;
    private String mEmail;
    private String mPhone;



    public Person() {
        this(UUID.randomUUID());
    }

    public Person(UUID id) {
        mId = id;
    }

    public String getName() {
        return mFirstname;
    }

    public String getUserName() {
        return mLastName;
    }

    public int getAge() {
        return mAge;
    }

    public String getAdress() {
        return mAdress;
    }

    public UUID getId() {
        return mId;
    }

    public void setFirstname(String firstname) {
        mFirstname = firstname;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public void setAge(int age) {
        mAge = age;
    }

    public void setAdress(String adress) {
        mAdress = adress;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }
}
