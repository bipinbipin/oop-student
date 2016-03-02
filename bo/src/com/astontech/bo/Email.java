package com.astontech.bo;

/**
 * Created by bipibuta1 on 2/18/2016.
 */
public class Email extends BaseBO {

    //region PROPERTIES

    private int EmailId;
    private String EmailAddress;
    private EntityType EmailType;

    //endregion

    //region CONSTRUCTORS

    public Email() {
        this.setEmailType(new EntityType());
    }

    public Email(String emailAddress) {
        this.setEmailType(new EntityType());
        this.setEmailAddress(emailAddress);
    }

    //endregion

    //region GETTERS / SETTERS

    public int getEmailId() {
        return EmailId;
    }

    public void setEmailId(int emailId) {
        EmailId = emailId;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public EntityType getEmailType() {
        return EmailType;
    }

    public void setEmailType(EntityType emailType) {
        EmailType = emailType;
    }

    //endregion

}
