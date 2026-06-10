package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.dao.*;
import com.strongBeton.strongBeton.entity.coach.ClientCoach;
import com.strongBeton.strongBeton.entity.coach.Coach;
import com.strongBeton.strongBeton.entity.user.Role;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.entity.workout.Workout;
import com.strongBeton.strongBeton.enums.CoachStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoachServiceImpl implements CoachService{
    CoachRepository coachRepository;
    CoachRatingRepository coachRatingRepository;
    InjuriesRepository injuriesRepository;
    UserRepository userRepository;
    ClientCoachRepository clientCoachRepository;
    WorkoutRepository workoutRepository;
    RoleRepository roleRepository;

    @Autowired
    public CoachServiceImpl(CoachRepository coachRepository, CoachRatingRepository coachRatingRepository,
                            InjuriesRepository injuriesRepository, UserRepository userRepository,
                            ClientCoachRepository clientCoachRepository, RoleRepository roleRepository,
                            WorkoutRepository workoutRepository)  {
        this.coachRepository = coachRepository;
        this.coachRatingRepository = coachRatingRepository;
        this.injuriesRepository = injuriesRepository;
        this.userRepository = userRepository;
        this.clientCoachRepository = clientCoachRepository;
        this.roleRepository = roleRepository;
        this.workoutRepository = workoutRepository;
    }

    @Transactional
    @Override
    public boolean promoteToCoachAccount(User user){
        Optional<Coach> coachResult = this.coachRepository.findByUser(user);
        if (!coachResult.isPresent()) {
            Role role = roleRepository.findByRoleName("Betonovoz")
                    .orElseThrow(() -> new EntityNotFoundException("No role like this"));
            user.setRole(role);
            Coach coach = new Coach(user, LocalDateTime.now(), LocalDateTime.now());
            this.userRepository.save(user);
            this.coachRepository.save(coach);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean inviteToBeACoach(User user, int coachId){
        Coach coach = this.coachRepository.findById(coachId).orElseThrow(() -> new EntityNotFoundException("No such a coach"));
        Optional<ClientCoach> clientCoachRes = this.clientCoachRepository.findByClientAndCoach(user.getId(), coach.getId());

        ClientCoach clientCoach;

        if (clientCoachRes.isPresent()) {
            clientCoach = clientCoachRes.get();
            if (clientCoach.getStatus() == CoachStatus.PENDING) {
                return false;
            }else if(clientCoach.getStatus() == CoachStatus.CANCELLED){
                clientCoach.setStatus(CoachStatus.PENDING);
                clientCoach.setStartDate(LocalDate.now());
                clientCoach.setEndData(null);
                this.clientCoachRepository.save(clientCoach);
                return true;
            }
        }else{
            clientCoach = new ClientCoach();
            clientCoach.setCoach(coach);
            clientCoach.setClient(user);
            clientCoach.setStatus(CoachStatus.PENDING);
            clientCoach.setStartDate(LocalDate.now());
            clientCoach.setEndData(null);
            this.clientCoachRepository.save(clientCoach);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean acceptToBeACoach(User user, int clientId){
        Coach coach = this.coachRepository.findByUser(user).orElseThrow();
        Optional<ClientCoach> clientCoachRes = this.clientCoachRepository.findByClientAndCoach(clientId, coach.getId());
        ClientCoach clientCoach;

        if(clientCoachRes.isPresent()){

            clientCoach  = clientCoachRes.get();
            if (clientCoach.getStatus() == CoachStatus.PENDING) {
                List<Workout> workouts = this.workoutRepository.findByUserId(clientId);
                for(Workout workout : workouts) {
                    workout.setCoach(coach);
                }
                clientCoach.setStatus(CoachStatus.ACTIVE);
                clientCoach.setStartDate(LocalDate.now());

                this.clientCoachRepository.save(clientCoach);
                return true;
            }

        }
        return false;
    }

    @Transactional
    @Override
    public boolean fireACoach(User user, int coachId){
        Coach coach = this.coachRepository.findById(coachId).orElseThrow(() -> new EntityNotFoundException("No such a coach"));
        Optional<ClientCoach> clientCoachRes = this.clientCoachRepository.findByClientAndCoach(user.getId(), coach.getId());
        ClientCoach clientCoach;

        if(clientCoachRes.isPresent()){
            clientCoach  = clientCoachRes.get();
            if (clientCoach.getStatus() == CoachStatus.ACTIVE){
                clientCoach.setStatus(CoachStatus.CANCELLED);
                clientCoach.setEndData(LocalDate.now());
                this.clientCoachRepository.save(clientCoach);
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public UUID verifyClientAccess(User user, String username){
        Optional<Coach> coachRes = this.coachRepository.findByUser(user);
        if(coachRes.isPresent()){
            User clientRes = this.userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("There's no entity"));
            Optional<ClientCoach> clientCoachRes = this.clientCoachRepository.findByClientAndCoach(clientRes.getId(), coachRes.get().getId());
            return clientCoachRes.isPresent() && clientCoachRes.get().getStatus() == CoachStatus.ACTIVE ? clientRes.getUuid() : null;
        }
        return null;
    }

    @Override
    public boolean checkWorkoutFromCoach(User user, UUID uuidClient) {
        User client = this.userRepository.findByUuid(uuidClient).orElseThrow();
        Coach coach = this.coachRepository.findByUser(user).orElseThrow();
        ClientCoach clientCoach = this.clientCoachRepository.findByClientAndCoach(client.getId(), coach.getId()).orElseThrow();
        return clientCoach.getStatus() == CoachStatus.ACTIVE;
    }


}
