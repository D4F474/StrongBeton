package com.strongBeton.strongBeton.dto.user;

import com.strongBeton.strongBeton.entity.social.FriendView;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String city;
    private int cm;
    private float kg;
    private LocalDate born_date;
    private String gender;
    private List<FriendView> friends;
    private String profilePhotoUrl;

    public UserDTO() {
    }

    public UserDTO(String username,
                   String firstName,
                   String lastName,
                   String city,
                   int cm,
                   float kg,
                   LocalDate born_date,
                   String gender,
                   List<FriendView> friends) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.cm = cm;
        this.kg = kg;
        this.born_date = born_date;
        this.gender = gender;
        this.friends = friends;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<FriendView> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendView> friends) {
        this.friends = friends;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
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
