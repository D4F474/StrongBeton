package com.strongBeton.strongBeton.rest;

import com.strongBeton.strongBeton.dto.StrengthScorePreviewRequestDTO;
import com.strongBeton.strongBeton.dto.StrengthScorePreviewResponseDTO;
import com.strongBeton.strongBeton.enums.ExerciseDifficulty;
import com.strongBeton.strongBeton.scoring.StrengthScoreCalculator;
import com.strongBeton.strongBeton.service.ScorePreviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.OptionalDouble;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    private final ScorePreviewService scorePreviewService;

    public ScoreController(ScorePreviewService scorePreviewService) {
        this.scorePreviewService = scorePreviewService;
    }

    @PostMapping("/preview")
    public ResponseEntity<StrengthScorePreviewResponseDTO> preview(
            @RequestBody StrengthScorePreviewRequestDTO request
    ) {
        return ResponseEntity.ok(scorePreviewService.preview(request));
    }
}
