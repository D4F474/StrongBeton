package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.InjuriesDTO;
import com.strongBeton.strongBeton.entity.Injuries;
import com.strongBeton.strongBeton.entity.User;

import java.util.List;

public interface InjuriesService {
    List<InjuriesDTO> getInjuriesForCurrentUser(User user);
    InjuriesDTO addInjuryForCurrentUser(User user, InjuriesDTO injuriesDTO);
}
