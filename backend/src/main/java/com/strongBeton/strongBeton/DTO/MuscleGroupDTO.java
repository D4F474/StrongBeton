package com.strongBeton.strongBeton.DTO;

public class MuscleGroupDTO {

    private int id;

    private String name;

    public MuscleGroupDTO() {
    }

    public MuscleGroupDTO(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "MuscleGroupDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

