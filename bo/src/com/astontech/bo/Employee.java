package com.astontech.bo;

import java.util.Date;

/**
 * Created by bipibuta1 on 2/17/2016.
 */
public class Employee extends Person {

    // PARAMETERS
    private int EmployeeId;
    private Date HireDate;
    private Date TermDate;

    //CONSTRUCTORS

    public Employee() {}

    public Employee(String firstName, String lastName) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public Employee(int employeeId, String firstName) {
        this.setFirstName(firstName);
    }

    public Employee(String lastName) {
        this.setLastName(lastName);
    }

    //GETTERS / SETTERS
    public int getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(int employeeId) {
        EmployeeId = employeeId;
    }

    public Date getHireDate() {
        return HireDate;
    }

    public void setHireDate(Date hireDate) {
        HireDate = hireDate;
    }

    public Date getTermDate() {
        return TermDate;
    }

    public void setTermDate(Date termDate) {
        TermDate = termDate;
    }




}
