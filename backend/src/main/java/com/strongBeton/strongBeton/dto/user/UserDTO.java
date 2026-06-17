package com.strongBeton.strongBeton.dto.user;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class UserDTO {
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 45)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    private String username;

    @Size(max = 45)
    private String firstName;

    @Size(max = 45)
    private String lastName;

    @Min(1)
    @Max(260)
    private int cm;

    @DecimalMin(value = "30.1")
    @DecimalMax(value = "350.0")
    private float kg;

    private LocalDate bornDate;

    @Pattern(regexp = "^(Male|Female)$")
    private String gender;

    private List<FriendViewDTO> friends;

    @Size(max = 2048)
    private String profilePhotoUrl;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    private String role;

    public UserDTO() {
    }

    public UserDTO(UUID id,String username,
                   String firstName,
                   String lastName,
                   int cm,
                   float kg,
                   LocalDate born_date,
                   String gender,
                   List<FriendViewDTO> friends,
                   String email) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cm = cm;
        this.kg = kg;
        this.bornDate = born_date;
        this.gender = gender;
        this.friends = friends;
        this.email = email;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public List<FriendViewDTO> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendViewDTO> friends) {
        this.friends = friends;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cm=" + cm +
                ", kg=" + kg +
                ", born_date=" + bornDate +
                ", gender='" + gender + '\'' +
                '}';
    }
}
