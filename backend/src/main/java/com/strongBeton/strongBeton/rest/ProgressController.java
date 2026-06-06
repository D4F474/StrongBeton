package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.ProgressPredictionDTO;
import com.strongBeton.strongBeton.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/{userUuid}/exercise/{exerciseId}")
    public ResponseEntity<ProgressPredictionDTO> predictExerciseProgress(
            @PathVariable UUID userUuid,
            @PathVariable int exerciseId
    ) {
        return ResponseEntity.ok(
                progressService.predictExerciseProgress(userUuid, exerciseId)
        );
    }
}
