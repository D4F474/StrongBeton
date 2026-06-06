package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dto.StrengthScorePreviewRequestDTO;
import com.strongBeton.strongBeton.dto.StrengthScorePreviewResponseDTO;

public interface ScorePreviewService {
    public StrengthScorePreviewResponseDTO preview(StrengthScorePreviewRequestDTO request);
}
