package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.InjuriesDTO;
import com.strongBeton.strongBeton.dao.InjuriesRepository;
import com.strongBeton.strongBeton.dao.MuscleGroupRepository;
import com.strongBeton.strongBeton.entity.Injuries;
import com.strongBeton.strongBeton.entity.MuscleGroup;
import com.strongBeton.strongBeton.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InjuriesServiceImpl implements InjuriesService {

    private InjuriesRepository injuriesRepository;
    private MuscleGroupRepository muscleGroupRepository;

    @Autowired
    public InjuriesServiceImpl(InjuriesRepository injuriesRepository,
                               MuscleGroupRepository muscleGroupRepository) {
        this.injuriesRepository = injuriesRepository;
        this.muscleGroupRepository = muscleGroupRepository;
    }

    @Override
    public List<InjuriesDTO> getInjuriesForCurrentUser(User user) {
        List<Injuries> injuries = this.injuriesRepository.findAllByUserId(user.getId()).orElseThrow();
        List<InjuriesDTO> result = new ArrayList<>();
        for(Injuries injury : injuries){
            InjuriesDTO injuriesDTO = new InjuriesDTO();
            injuriesDTO.setName(injury.getName());
            injuriesDTO.setMuscleGroup(injury.getMuscleGroup().getMuscleGroupName());
            injuriesDTO.setDescription(injury.getDescription());
            result.add(injuriesDTO);
        }
        return result;
    }

    public InjuriesDTO addInjuryForCurrentUser(User user, InjuriesDTO injuriesDTO){
        Injuries injuries = new Injuries();
        injuries.setName(injuriesDTO.getName());
        injuries.setUser(user);
        System.out.println(injuriesDTO.getMuscleGroup());
        MuscleGroup muscleGroup = this.muscleGroupRepository
                .findByMuscleGroupName(injuriesDTO.getMuscleGroup())
                .orElseThrow(() -> new EntityNotFoundException("Muscle group not found: " + injuriesDTO.getMuscleGroup()));
        System.out.println(muscleGroup);
        injuries.setMuscleGroup(muscleGroup);
        injuries.setDescription(injuriesDTO.getDescription());

        this.injuriesRepository.save(injuries);
        return injuriesDTO;
    }
}
