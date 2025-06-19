package com.strongBeton.strongBeton.DTO;

public class ExerciseDTO {

    private int id;
    private String name;

    public ExerciseDTO() {
    }

    public ExerciseDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExerciseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


}
