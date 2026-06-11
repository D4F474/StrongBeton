package com.strongBeton.strongBeton.service.workout;

import com.strongBeton.strongBeton.dao.WorkoutDetailsRepository;
import com.strongBeton.strongBeton.dao.WorkoutRepository;
import com.strongBeton.strongBeton.dto.workout.SetsDTO;
import com.strongBeton.strongBeton.dao.SetsRepository;
import com.strongBeton.strongBeton.entity.workout.Sets;
import com.strongBeton.strongBeton.entity.workout.Workout;
import com.strongBeton.strongBeton.entity.workout.WorkoutDetails;
import com.strongBeton.strongBeton.scoring.StrengthScoreCalculator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
public class SetsServiceImpl implements SetsService {

    private final ModelMapper modelMapper;
    private final SetsRepository setsRepository;
    private final StrengthScoreCalculator strengthScoreCalculator;
    private final WorkoutScoreUpdaterService workoutScoreUpdater;
    private final WorkoutDetailsRepository workoutDetailsRepository;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public SetsServiceImpl(
            ModelMapper modelMapper,
            SetsRepository setsRepository,
            StrengthScoreCalculator strengthScoreCalculator,
            WorkoutScoreUpdaterService workoutScoreUpdater,
            WorkoutDetailsRepository workoutDetailsRepository,
            WorkoutRepository workoutRepository
    ) {
        this.modelMapper = modelMapper;
        this.setsRepository = setsRepository;
        this.strengthScoreCalculator = strengthScoreCalculator;
        this.workoutScoreUpdater = workoutScoreUpdater;
        this.workoutDetailsRepository = workoutDetailsRepository;
        this.workoutRepository = workoutRepository;
    }

    @Override
    @Transactional
    public SetsDTO saveSet(SetsDTO setsDTO) {
        WorkoutDetails workoutDetails = validateSetAndGetWorkoutDetails(setsDTO);
        ensureWorkoutIsEditable(workoutDetails);

        LocalDateTime now = LocalDateTime.now();

        Sets sets;

        if (setsDTO.getId() > 0) {
            sets = setsRepository.findById(setsDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Set not found"));

            if (sets.getWorkoutDetailsId() != setsDTO.getWorkoutDetailsId()) {
                throw new IllegalStateException("Cannot move set to another workout detail");
            }
        } else {
            sets = new Sets();
            sets.setCreatedAt(now);
            sets.setWorkoutDetailsId(setsDTO.getWorkoutDetailsId());
        }

        OptionalDouble oneRepMaxOptional = strengthScoreCalculator.estimateOneRepMax(
                setsDTO.getKg(),
                setsDTO.getReps()
        );

        if (oneRepMaxOptional.isPresent()) {
            sets.setEstimatedOneRepMax(
                    strengthScoreCalculator.round(oneRepMaxOptional.getAsDouble(), 2)
            );
        } else {
            sets.setEstimatedOneRepMax(null);
        }

        sets.setUpdatedAt(now);
        sets.setSetNumber(setsDTO.getSetNumber());
        sets.setKg(setsDTO.getKg());
        sets.setReps(setsDTO.getReps());

        Sets saved = setsRepository.save(sets);

        workoutScoreUpdater.recalculateWorkoutDetailScore(
                saved.getWorkoutDetailsId()
        );

        return modelMapper.map(saved, SetsDTO.class);
    }

    @Override
    public List<SetsDTO> findSetsByWorkoutId(int workoutDetailsId) {
        return setsRepository.findSetsByWorkoutDetailsId(workoutDetailsId)
                .stream()
                .map(set -> modelMapper.map(set, SetsDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSet(int theId) {
        Sets set = setsRepository.findById(theId)
                .orElseThrow(() -> new EntityNotFoundException("Set not found"));

        int workoutDetailsId = set.getWorkoutDetailsId();

        WorkoutDetails workoutDetails = workoutDetailsRepository.findById(workoutDetailsId)
                .orElseThrow(() -> new EntityNotFoundException("Workout details not found"));

        ensureWorkoutIsEditable(workoutDetails);

        setsRepository.delete(set);

        workoutScoreUpdater.recalculateWorkoutDetailScore(workoutDetailsId);
    }

    private WorkoutDetails validateSetAndGetWorkoutDetails(SetsDTO sets) {
        if (sets.getReps() <= 0) {
            throw new IllegalArgumentException("Reps must be positive");
        }

        if (sets.getKg() <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }

        if (sets.getSetNumber() <= 0) {
            throw new IllegalArgumentException("Set number must be positive");
        }

        if (sets.getWorkoutDetailsId() <= 0) {
            throw new IllegalArgumentException("Workout details id is required");
        }

        return workoutDetailsRepository.findById(sets.getWorkoutDetailsId())
                .orElseThrow(() -> new EntityNotFoundException("Workout details not found"));
    }

    private void ensureWorkoutIsEditable(WorkoutDetails workoutDetails) {
        Workout workout = workoutRepository.findById(workoutDetails.getWorkoutId())
                .orElseThrow(() -> new EntityNotFoundException("Workout not found"));

        if (workout.getStatus() == null) {
            return;
        }

        String status = workout.getStatus().toString();

        if ("FINISHED".equalsIgnoreCase(status)) {
            throw new IllegalStateException("Finished workout cannot be modified");
        }
    }

}
