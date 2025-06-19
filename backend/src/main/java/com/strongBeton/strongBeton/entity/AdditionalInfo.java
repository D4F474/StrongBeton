package com.strongBeton.strongBeton.entity;

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

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH
            })
    @JoinColumn(name="city_id")
    private City city;

    @Column(name = "street_info")
    private String streetInfo;

    @Column(name = "cm")
    private int cm;

    @Column(name = "kg")
    private float kg;

    @Column(name = "born_date")
    private LocalDate bornDate;

    @Column(name = "gender")
    private boolean gender;

    public AdditionalInfo() {
    }

    public AdditionalInfo(String firstName,
                          String lastName,
                          City city,
                          String streetInfo,
                          int cm,
                          float kg,
                          LocalDate bornDate,
                          boolean gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.streetInfo = streetInfo;
        this.cm = cm;
        this.kg = kg;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getStreetInfo() {
        return streetInfo;
    }

    public void setStreetInfo(String streetInfo) {
        this.streetInfo = streetInfo;
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

    public LocalDate getBornDate() {
        return bornDate;
    }

    public void setBornDate(LocalDate bornDate) {
        this.bornDate = bornDate;
    }

    public String isGender() {
        if(gender){
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
                ", city=" + city +
                ", streetInfo='" + streetInfo + '\'' +
                '}';
    }
}
