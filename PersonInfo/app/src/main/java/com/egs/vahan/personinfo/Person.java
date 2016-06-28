package com.egs.vahan.personinfo;


/**
 * Created by vahan on 6/20/16.
 */
public class Person {

    private int mId;
    private String mFirstname;
    private String mLastName;
    private String mAdress;
    private String mEmail;
    private String mPhone;


    public String getFirstname() {
        return mFirstname;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getAdress() {
        return mAdress;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setFirstname(String firstname) {
        mFirstname = firstname;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
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
