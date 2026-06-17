package com.strongBeton.strongBeton.dto.stats;

import java.time.LocalDateTime;

public class WeightHistoryDTO {
    private float kg;
    private LocalDateTime loggedAt;

    public WeightHistoryDTO() {
    }

    public WeightHistoryDTO(float kg, LocalDateTime loggedAt) {
        this.kg = kg;
        this.loggedAt = loggedAt;
    }

    public float getKg() {
        return kg;
    }

    public void setKg(float kg) {
        this.kg = kg;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }
}
