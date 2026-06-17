package com.strongBeton.strongBeton.mapper;

import com.strongBeton.strongBeton.dto.workout.ExerciseDTO;
import com.strongBeton.strongBeton.entity.workout.Exercise;
import com.strongBeton.strongBeton.entity.workout.MuscleGroup;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<MuscleGroup, String> muscleGroupToName = context -> {
            MuscleGroup muscleGroup = context.getSource();
            return muscleGroup == null ? null : muscleGroup.getMuscleGroupName();
        };

        modelMapper.typeMap(Exercise.class, ExerciseDTO.class)
                .addMappings(mapper -> mapper
                        .using(muscleGroupToName)
                        .map(Exercise::getMuscleGroup, ExerciseDTO::setMuscleGroup));

        return modelMapper;
    }

}
