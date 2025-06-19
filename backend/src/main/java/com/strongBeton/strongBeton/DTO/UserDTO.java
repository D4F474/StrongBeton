package com.strongBeton.strongBeton.DTO;

import com.strongBeton.strongBeton.entity.City;

import java.time.LocalDate;

public class UserDTO {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String city;
    private int cm;
    private float kg;
    private LocalDate born_date;
    private String gender;
    private float totalTonnage_kg;
    private float totalTonnageKgThisMonth;
    private int trainingCounter;
    private int trainingCounterThisMonth;

    public UserDTO() {
    }

    public UserDTO(String username,
                   String firstName,
                   String lastName,
                   String city,
                   int cm,
                   float kg,
                   LocalDate born_date,
                   String gender) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.cm = cm;
        this.kg = kg;
        this.born_date = born_date;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCm() {
        return cm;
    }

    public void setCm(int cm) {
        this.cm = cm;
    }

    public float getKg() {
        return kg;
    }

    public void setKg(float kg) {
        this.kg = kg;
    }

    public LocalDate getBorn_date() {
        return born_date;
    }

    public void setBorn_date(LocalDate born_date) {
        this.born_date = born_date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTotalTonnage_kg() {
        return totalTonnage_kg;
    }

    public void setTotalTonnage_kg(float totalTonnage_kg) {
        this.totalTonnage_kg = totalTonnage_kg;
    }

    public float getTotalTonnageKgThisMonth() {
        return totalTonnageKgThisMonth;
    }

    public void setTotalTonnageKgThisMonth(float totalTonnageKgThisMonth) {
        this.totalTonnageKgThisMonth = totalTonnageKgThisMonth;
    }

    public int getTrainingCounter() {
        return trainingCounter;
    }

    public void setTrainingCounter(int trainingCounter) {
        this.trainingCounter = trainingCounter;
    }

    public int getTrainingCounterThisMonth() {
        return trainingCounterThisMonth;
    }

    public void setTrainingCounterThisMonth(int trainingCounterThisMonth) {
        this.trainingCounterThisMonth = trainingCounterThisMonth;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", cm=" + cm +
                ", kg=" + kg +
                ", born_date=" + born_date +
                ", gender='" + gender + '\'' +
                '}';
    }
}
