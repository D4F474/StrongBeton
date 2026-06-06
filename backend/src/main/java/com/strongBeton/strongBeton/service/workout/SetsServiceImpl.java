package com.strongBeton.strongBeton.service.workout;

import com.strongBeton.strongBeton.dao.WorkoutDetailsRepository;
import com.strongBeton.strongBeton.dto.workout.SetsDTO;
import com.strongBeton.strongBeton.dao.SetsRepository;
import com.strongBeton.strongBeton.entity.workout.Sets;
import com.strongBeton.strongBeton.scoring.StrengthScoreCalculator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public SetsServiceImpl(
            ModelMapper modelMapper,
            SetsRepository setsRepository,
            StrengthScoreCalculator strengthScoreCalculator,
            WorkoutScoreUpdaterService workoutScoreUpdater,
            WorkoutDetailsRepository workoutDetailsRepository
    ) {
        this.modelMapper = modelMapper;
        this.setsRepository = setsRepository;
        this.strengthScoreCalculator = strengthScoreCalculator;
        this.workoutScoreUpdater = workoutScoreUpdater;
        this.workoutDetailsRepository = workoutDetailsRepository;
    }

    @Override
    @Transactional
    public SetsDTO saveSet(Sets sets) {
        validateSet(sets);
        OptionalDouble oneRepMaxOptional = strengthScoreCalculator.estimateOneRepMax(
                sets.getKg(),
                sets.getReps()
        );

        if (oneRepMaxOptional.isPresent()) {
            sets.setEstimatedOneRepMax(
                    strengthScoreCalculator.round(oneRepMaxOptional.getAsDouble(), 2)
            );
        } else {
            sets.setEstimatedOneRepMax(null);
        }

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

        setsRepository.delete(set);

        workoutScoreUpdater.recalculateWorkoutDetailScore(workoutDetailsId);
    }

    private void validateSet(Sets sets) {
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

        if (!workoutDetailsRepository.existsById(sets.getWorkoutDetailsId())) {
            throw new EntityNotFoundException("Workout details not found");
        }
    }

}
