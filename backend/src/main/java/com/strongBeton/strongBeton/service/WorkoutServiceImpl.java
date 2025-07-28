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
    public WorkoutDTO save(WorkoutDTO workoutDTO, int userId) {
        Workout workout = new Workout();
        workout.setUser(userRepository.findById(userId).get());
        workout.setDate(workoutDTO.getDate());
        workout.setWorkoutTemplate(new WorkoutTemplate(workoutDTO.getWorkoutName()));
             return modelMapper.map(workoutRepository.save(workout), WorkoutDTO.class);
        }

    @Override
    @Transactional
        public List<WorkoutDTO> findBySearchbar(int userId, String keyword){
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
    public Map<String,List<WorkoutDTO>> findByUserId(int userId) {
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
            }else{
                List<WorkoutDTO> updateList = result.get(workoutDTO.getWorkoutName());
                updateList.add(workoutDTO);
                result.put(workoutDTO.getWorkoutName(), updateList);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteWorkoutById(int theId) {
        Optional<WorkoutDetails> workoutDetails = workoutDetailsRepository.findById(theId);
        if (!workoutDetails.isPresent()){
            List<WorkoutDetailsDTO> workoutDetailsDTO = Collections.singletonList(modelMapper.map(workoutDetails, WorkoutDetailsDTO.class));
            for (WorkoutDetailsDTO detailsDTO :workoutDetailsDTO ) {
                Optional<Sets> sets = this.setsRepository.findById(detailsDTO.getId());
                if(sets.isPresent()){
                    List<SetsDTO> setsDTO = Collections.singletonList(modelMapper.map(sets.get(), SetsDTO.class));
                for (SetsDTO set : setsDTO) {
                    this.setsRepository.deleteById(set.getId());
                }
                this.workoutDetailsRepository.deleteById(detailsDTO.getId());
                }
            }
        }

        workoutRepository.deleteById(theId);
    }
}