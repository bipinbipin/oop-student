package com.astontech.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import common.helpers.*;

/**
 * Created by bipibuta1 on 2/17/2016.
 */
public class Person extends BaseBO implements Serializable{

    //region PROPERTIES
    // PersonId
    private int PersonId;
    // Title
    private String Title;
    // First Name
    private String FirstName;
    private String MiddleName;
    // Last Name
    private String LastName;
    // DisplayFirstName
    private String DisplayFirstName;
    // Gender
    private String Gender;

    private Date BirthDate;
    private transient String SSN;
    // List of Email objects
    private List<Email> Emails;

    private static final long serialVersionUID = 54622233600l;
    //endregion

    //region CONSTRUCTORS

    public Person() {
        this.Emails = new ArrayList<>();
    }

    //endregion

    //region GETTERS / SETTERS
    public void setPersonId(int personId) {
        this.PersonId = personId;
    }

    public int getPersonId() {
        return this.PersonId;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getFirstName() {
        return this.FirstName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getDisplayFirstName() {
        return DisplayFirstName;
    }

    public void setDisplayFirstName(String displayFirstName) {
        DisplayFirstName = displayFirstName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public List<Email> getEmails() {
        return this.Emails;
    }

    public void setEmails(List<Email> emailList) {
        this.Emails = emailList;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public Date getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(Date birthDate) {
        BirthDate = birthDate;
    }

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }
    //endregion

    //region CUSTOM METHODS


    public String GetFullName() {
        if(StringHelper.isNullOrEmpty(this.FirstName) && StringHelper.isNullOrEmpty(this.LastName))
            return "No Name Set";
        else
        {
            if(StringHelper.isNullOrEmpty(this.FirstName))
                return this.LastName;
            else if(StringHelper.isNullOrEmpty(this.LastName))
                return this.FirstName;
            else
                return this.FirstName + " " + this.LastName;
        }
    }

    public String ToString() {
        return "PersonId=" + this.PersonId + " Full Name=" + this.GetFullName() + " DOB=" + this.BirthDate + " SSN=" + this.SSN;
    }

    //endregion

}
