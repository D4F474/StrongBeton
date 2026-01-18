package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.*;
import com.strongBeton.strongBeton.dao.*;
import com.strongBeton.strongBeton.entity.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private static final Logger log = LoggerFactory.getLogger(WorkoutServiceImpl.class);
    ModelMapper modelMapper;
    WorkoutDetailsRepository workoutDetailsRepository;
    WorkoutRepository workoutRepository;
    SetsRepository setsRepository;
    UserRepository userRepository;

    public WorkoutServiceImpl(ModelMapper modelMapper,
                              WorkoutDetailsRepository workoutDetailsRepository,
                              WorkoutRepository workoutRepository,
                              SetsRepository setsRepository,
                              UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.workoutDetailsRepository = workoutDetailsRepository;
        this.workoutRepository = workoutRepository;
        this.setsRepository = setsRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public WorkoutDTO save(WorkoutDTO workoutDTO, UUID userId) {
        Workout workout = new Workout();
        workout.setUser(userRepository.findById(userId).get());
        workout.setDate(LocalDate.now());
        workout.setWorkoutTemplate(new WorkoutTemplate(workoutDTO.getWorkoutName()));
             return modelMapper.map(workoutRepository.save(workout), WorkoutDTO.class);
        }

    @Override
    @Transactional
        public List<WorkoutDTO> findBySearchbar(UUID userId, String keyword){
            return workoutRepository.searchWorkoutByUser(userId, keyword)
                    .stream()
                    .map(workout ->{
                        WorkoutDTO workoutDTO = modelMapper.map(workout, WorkoutDTO.class);
                        Double sumOfKg = workoutRepository.getTonnageForWorkout(workout.getId());
                        workoutDTO.setTotal_tonnage_kg(sumOfKg != null ? sumOfKg : 0.0 );
                        System.out.println(workoutDTO.toString());
                        return workoutDTO;
                    })
                    .collect(Collectors.toList());
        }
    @Override
    public Map<String,List<WorkoutDTO>> findByUserId(UUID userId) {
        List<WorkoutDTO> dtos = workoutRepository.findByUserId(userId)
                .stream()
                .map(workout ->{
                    WorkoutDTO workoutDTO = modelMapper.map(workout, WorkoutDTO.class);
                    Double sumOfKg = workoutRepository.getTonnageForWorkout(workout.getId());
                    workoutDTO.setTotal_tonnage_kg(sumOfKg != null ? sumOfKg : 0.0 );
                    workoutDTO.setWorkoutName(workout.getWorkoutTemplate().getWorkout_name());
                    return workoutDTO;
                })
                .collect(Collectors.toList());
        Map<String, List<WorkoutDTO>> result = new HashMap<>();
        for(WorkoutDTO workoutDTO : dtos){
            if(!result.containsKey(workoutDTO.getWorkoutName())){
                result.put(workoutDTO.getWorkoutName(), new ArrayList<>());
            }
                List<WorkoutDTO> updateList = result.get(workoutDTO.getWorkoutName());
                updateList.add(workoutDTO);
                result.put(workoutDTO.getWorkoutName(), updateList);

        }
        return result;
    }
//TODO REDAKTIRAI TOZI METOD DA TRIE PO-DOBRE WORKOUT-A
    @Override
    @Transactional
    public void deleteWorkoutById(UUID theId) {
        /*
        Optional<WorkoutDetails> workoutDetails = workoutDetailsRepository.findById(theId);
        if (!workoutDetails.isPresent()){
            List<WorkoutDetails> workoutDetailsList = workoutDetails.stream().toList();
            for (WorkoutDetails details :workoutDetailsList) {
                Optional<Sets> sets = this.setsRepository.findById(details.getId());
                if(sets.isPresent()){
                    List<Sets> setsList = sets.stream().toList();
                for (Sets set : setsList) {
                    this.setsRepository.deleteById(set.getId());
                }
                this.workoutDetailsRepository.deleteById(details.getId());
                }
            }
        }

        workoutRepository.deleteById(theId);
         */
    }
}