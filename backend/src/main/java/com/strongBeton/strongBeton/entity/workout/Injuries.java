package com.strongBeton.strongBeton.entity.workout;

import com.strongBeton.strongBeton.entity.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "injuries")
public class Injuries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "muscle_group")
    private MuscleGroup muscleGroup;
    @Column(name = "description")
    private String description;

    public Injuries() {
    }

    public Injuries(User user, String name, MuscleGroup muscleGroup, String description) {
        this.user = user;
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(MuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
