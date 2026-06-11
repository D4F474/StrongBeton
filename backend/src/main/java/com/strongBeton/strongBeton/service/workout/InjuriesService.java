package com.strongBeton.strongBeton.service.workout;

import com.strongBeton.strongBeton.dto.workout.InjuriesDTO;
import com.strongBeton.strongBeton.entity.user.User;

import java.util.List;

public interface InjuriesService {
    List<InjuriesDTO> getInjuriesForCurrentUser(User user);
    InjuriesDTO addInjuryForCurrentUser(User user, InjuriesDTO injuriesDTO);
}
