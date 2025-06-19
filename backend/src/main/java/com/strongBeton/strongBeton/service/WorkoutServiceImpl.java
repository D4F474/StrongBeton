package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.*;
import com.strongBeton.strongBeton.dao.*;
import com.strongBeton.strongBeton.entity.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private static final Logger log = LoggerFactory.getLogger(WorkoutServiceImpl.class);
    ModelMapper modelMapper;
    WorkoutDetailsRepository workoutDetailsRepository;
    WorkoutRepository workoutRepository;
    ExerciseRepository exerciseRepository;
    SetsRepository setsRepository;
    MuscleGroupRepository muscleGroupRepository;
    LeaderBoardRepository leaderBoardRepository;
    UserRepository userRepository;


    @Autowired
    public WorkoutServiceImpl(ModelMapper modelMapper,
                              WorkoutDetailsRepository workoutDetailsRepository,
                              WorkoutRepository workoutRepository,
                              ExerciseRepository exerciseRepository,
                              SetsRepository setsRepository,
                              MuscleGroupRepository muscleGroupRepository,
                              UserRepository userRepository,
                              LeaderBoardRepository leaderBoardRepository) {
        this.modelMapper = modelMapper;
        this.workoutDetailsRepository = workoutDetailsRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.setsRepository = setsRepository;
        this.muscleGroupRepository = muscleGroupRepository;
        this.userRepository = userRepository;
        this.leaderBoardRepository = leaderBoardRepository;
    }

    public User findUserByFromUserId(int theId){
        User user = null;
        if( userRepository.findById(theId).isPresent())
        {
            user =  userRepository.findById(theId).get();
        }
        return user;
    }

    @Override
    public List<WorkoutDTO> findAll() {
        return workoutRepository.findAll()
                .stream()
                .map(workout -> {
                   WorkoutDTO workoutDTO = modelMapper.map(workout, WorkoutDTO.class);

                    return workoutDTO;
                }).collect(Collectors.toList());
    }


    @Override
    public List<LeaderBoard>findLeaderBoardForUser(){
       return leaderBoardRepository.findAll();
    }
    @Override
    public WorkoutDTO findById(int theId) {

        return modelMapper.map(workoutRepository.findById(theId), WorkoutDTO.class);
    }

    @Override
    @Transactional
    public WorkoutDTO save(Workout workout) {
        System.out.println("Im from service workout saver!");
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
    public List<WorkoutDTO> findByUserId(int userId) {
        List<WorkoutDTO> dtos = workoutRepository.findByUserId(userId)
                .stream()
                .map(workout ->{
                    WorkoutDTO workoutDTO = modelMapper.map(workout, WorkoutDTO.class);
                    Double sumOfKg = workoutRepository.getTonnageForWorkout(workout.getId());
                    workoutDTO.setTotal_tonnage_kg(sumOfKg != null ? sumOfKg : 0.0 );
                    return workoutDTO;
                })
                .collect(Collectors.toList());


        return dtos;
    }

    //TODO fix it
    @Override
    public WorkoutDTO addExerciseToWorkout(WorkoutDetails workoutDetails) {
        WorkoutDetails workoutToUpdate = workoutDetailsRepository.findExerciseById(workoutDetails.getExercise().getId());
        if(workoutToUpdate != null){
            workoutToUpdate.setExercise(workoutDetails.getExercise());
            return modelMapper.map(workoutToUpdate, WorkoutDTO.class);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteWorkoutById(int theId) {
        workoutRepository.deleteById(theId);
    }

    @Override
    @Transactional
    public WorkoutDetailsDTO saveWorkoutDetails(WorkoutDetails workoutDetails) {
        if (workoutDetails.getExercise() != null) {
            Optional<Exercise> existingExercise = exerciseRepository.findByName(workoutDetails.getExercise().getName());
            if (existingExercise.isPresent()) {
                workoutDetails.setExercise(existingExercise.get());
            } else {
                exerciseRepository.save(workoutDetails.getExercise());
            }
        }

        if (workoutDetails.getMuscleGroup() != null) {
            Optional<MuscleGroup> existingMuscleGroup = muscleGroupRepository.findByMuscleGroupName(workoutDetails.getMuscleGroup().getMuscleGroupName());
            if (existingMuscleGroup.isPresent()) {
                workoutDetails.setMuscleGroup(existingMuscleGroup.get());
            } else {
                muscleGroupRepository.save(workoutDetails.getMuscleGroup());
            }
        }

        return modelMapper.map(workoutDetailsRepository.save(workoutDetails), WorkoutDetailsDTO.class);
    }


    @Override
    @Transactional
    public List<WorkoutDetailsDTO> findWorkoutDetailsById(int workoutId){
        return workoutDetailsRepository.findByWorkoutId(workoutId)
                .stream()
                .map(workout -> {
                    WorkoutDetailsDTO workoutDetailsDTO = modelMapper.map(workout, WorkoutDetailsDTO.class);
                    workoutDetailsDTO.setExercise(modelMapper.map(workout.getExercise(), ExerciseDTO.class));
                    workoutDetailsDTO.setMuscleGroup(workout.getMuscleGroup().getMuscleGroupName());
                    System.out.println(workoutDetailsDTO.getExercise());
                    return workoutDetailsDTO;
                })
                .toList();
    }

    @Override
    @Transactional
    public WorkoutDetailsDTO findWorkoutDetailById(int theId) {
        return modelMapper.map(workoutDetailsRepository.findById(theId), WorkoutDetailsDTO.class);
    }

    @Override
    @Transactional
    public SetsDTO saveSet(Sets sets) {
        return modelMapper.map(setsRepository.save(sets), SetsDTO.class);
    }

    @Override
    @Transactional
    public void  deleteWorkoutDetailsExerciseById(int theId) {
        workoutDetailsRepository.deleteByExerciseId(theId);
    }

    @Override
    @Transactional
    public void deleteSet(int theId) {
        setsRepository.deleteById(theId);
    }

    @Override
    public void deleteWorkoutDetailsById(int workoutDetailId) {
        workoutDetailsRepository.deleteById(workoutDetailId);
    }


    @Override
    public List<SetsDTO> findSetsByWorkoutId(int workoutId){
        return setsRepository.findSetsByWorkoutDetailsId(workoutId)
                .stream()
                .map(set -> {
                    SetsDTO setsDTO =modelMapper.map(set, SetsDTO.class);
                    return setsDTO;
                })
                .collect(Collectors.toList());
    }
}
