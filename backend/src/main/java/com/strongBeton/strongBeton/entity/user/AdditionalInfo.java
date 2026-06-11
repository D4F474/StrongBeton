package com.strongBeton.strongBeton.entity.user;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="additional_info")
public class AdditionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "cm")
    private int cm;

    @Column(name = "born_date")
    private LocalDate bornDate;

    @Column(name = "gender")
    private boolean gender;

    public AdditionalInfo() {
    }

    public AdditionalInfo(String firstName,
                          String lastName,
                          int cm,
                          LocalDate bornDate,
                          boolean gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cm = cm;
        this.bornDate = bornDate;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getCm() {
        return cm;
    }

    public void setCm(int cm) {
        this.cm = cm;
    }

    public LocalDate getBornDate() {
        return bornDate;
    }

    public void setBornDate(LocalDate bornDate) {
        this.bornDate = bornDate;
    }

    public String isGender() {
        if(!gender){
            return "Female";
        }
        return "Male";
    }

    public boolean getGender(){
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "AdditionalInfo{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
