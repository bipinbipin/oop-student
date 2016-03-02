package com.astontech.bo;

/**
 * Created by bipibuta1 on 2/17/2016.
 */
public class BaseBO {

    private int Id;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


    public String test_method() {
        return "super method";
    }
}
