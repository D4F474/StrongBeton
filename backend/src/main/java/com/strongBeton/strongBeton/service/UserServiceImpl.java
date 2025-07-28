package com.strongBeton.strongBeton.service;

import com.strongBeton.strongBeton.DTO.UserDTO;
import com.strongBeton.strongBeton.dao.*;
import com.strongBeton.strongBeton.entity.FriendView;
import com.strongBeton.strongBeton.entity.User;
import com.strongBeton.strongBeton.entity.UserTrainingDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AdditionalInfoRepository additionalInfoRepository;
    private final RoleRepository roleRepository;
    private final CityRepository cityRepository;
    private final UserTrainingDetailsRepository userTrainingDetails;
    private final FriendViewRepository friendViewRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AdditionalInfoRepository additionalInfoRepository,
                           RoleRepository roleRepository,
                           CityRepository cityRepository,
                           UserTrainingDetailsRepository userTrainingDetails,
                           FriendViewRepository friendViewRepository,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.additionalInfoRepository = additionalInfoRepository;
        this.roleRepository = roleRepository;
        this.cityRepository = cityRepository;
        this.userTrainingDetails = userTrainingDetails;
        this.friendViewRepository =friendViewRepository;
        this.modelMapper = modelMapper;
    }

    public UserDTO loadUserDataByUsername(String username){
        Optional<User> userExist = userRepository.findByUsername(username);
        User user = null;
        if(userExist.isPresent()){
            user = userExist.get();
            Optional<UserTrainingDetails> userTraningDataExist = userTrainingDetails.findById(user.getId());
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setFirstName(user.getAdditionalInfo().getFirstName());
            userDTO.setLastName(user.getAdditionalInfo().getLastName());
            userDTO.setBorn_date(user.getAdditionalInfo().getBornDate());
            userDTO.setKg(user.getAdditionalInfo().getKg());
            userDTO.setCm(user.getAdditionalInfo().getCm());
            userDTO.setCity(user.getAdditionalInfo().getCity().getCityName());
            userDTO.setGender(user.getAdditionalInfo().isGender());
            if(userTraningDataExist.isPresent()){
                UserTrainingDetails userTrainingDetail = userTraningDataExist.get();
                userDTO.setTotalTonnage_kg(userTrainingDetail.getTotalTonnage_kg());
                userDTO.setTotalTonnageKgThisMonth(userTrainingDetail.getTotalTonnageKgThisMonth());
                userDTO.setTrainingCounter(userTrainingDetail.getTrainingCounter());
                userDTO.setTrainingCounterThisMonth(userTrainingDetail.getTrainingCounterThisMonth());
                userDTO.setExerciseName(userTrainingDetail.getExerciseName());
                userDTO.setMostUsedExerciseCount(userTrainingDetail.getMostUsedExerciseCount());
            }

                // List<FriendView> friends = friendViewRepository.findAllFriendsVisual(user.getUsername());
                //userDTO.setFriends(friends);
                return userDTO;
        }
        return null;
    }

    @Override
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
}
