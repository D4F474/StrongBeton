package com.strongBeton.strongBeton.dto.user;

import java.util.UUID;

public class UserUpdateDTO {

    private String firstName;
    private String lastName;
    private float kg;


    public UserUpdateDTO() {
    }

    public UserUpdateDTO( String firstName, String lastName, float kg) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.kg = kg;
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

    public float getKg() {
        return kg;
    }

    public void setKg(float kg) {
        this.kg = kg;
    }


}
