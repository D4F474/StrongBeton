package com.strongBeton.strongBeton.entity.workout;


import com.strongBeton.strongBeton.enums.ExerciseDifficulty;
import com.strongBeton.strongBeton.enums.ExerciseEquipment;
import jakarta.persistence.*;

@Entity
@Table(name="exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "muscle_group_id")
    private MuscleGroup muscleGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_type", nullable = false)
    private ExerciseDifficulty exerciseDifficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_type", nullable = false)
    private ExerciseEquipment exerciseEquipment;

    @Column(name = "image_url")
    private String imageUrl;

    public Exercise() {
    }

    public Exercise(String name, MuscleGroup muscleGroup, ExerciseDifficulty exerciseDifficulty,
                    ExerciseEquipment exerciseEquipment, String imageUrl) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.exerciseDifficulty = exerciseDifficulty;
        this.exerciseEquipment = exerciseEquipment;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ExerciseDifficulty getExerciseDifficulty() {
        return exerciseDifficulty;
    }

    public void setExerciseDifficulty(ExerciseDifficulty exerciseDifficulty) {
        this.exerciseDifficulty = exerciseDifficulty;
    }

    public ExerciseEquipment getExerciseEquipment() {
        return exerciseEquipment;
    }

    public void setExerciseEquipment(ExerciseEquipment exerciseEquipment) {
        this.exerciseEquipment = exerciseEquipment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
