package com.strongBeton.strongBeton.service.user;

import com.strongBeton.strongBeton.dto.user.UserDTO;
import com.strongBeton.strongBeton.dao.*;
import com.strongBeton.strongBeton.dto.user.UserUpdateDTO;
import com.strongBeton.strongBeton.entity.user.AdditionalInfo;
import com.strongBeton.strongBeton.entity.user.KGLogs;
import com.strongBeton.strongBeton.entity.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.strongBeton.strongBeton.enums.PhotoType.PROFILE;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserTrainingDetailsRepository userTrainingDetails;
    private final CloudPhotoRepository photoRepository;
    private final KGLogRepository kgLogRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserTrainingDetailsRepository userTrainingDetails,
                           CloudPhotoRepository photoRepository,
                           KGLogRepository kgLogRepository) {
        this.userRepository = userRepository;
        this.userTrainingDetails = userTrainingDetails;
        this.photoRepository = photoRepository;
        this.kgLogRepository = kgLogRepository;
    }

    public UserDTO loadUserDataByEmail(String email){
            User user = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("No such user"));
            //Optional<UserTrainingDetails> userTrainingDataExist = userTrainingDetails.findById(user.getId());
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getUuid());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setFirstName(user.getAdditionalInfo().getFirstName());
            userDTO.setLastName(user.getAdditionalInfo().getLastName());
            userDTO.setBornDate(user.getAdditionalInfo().getBornDate());
            userDTO.setCm(user.getAdditionalInfo().getCm());
            userDTO.setGender(user.getAdditionalInfo().isGender());
            photoRepository.findByUserIdAndPhotoType(user.getId(), PROFILE.toString())
                    .ifPresent(photo -> userDTO.setProfilePhotoUrl(photo.getPhotoUrl()));
            KGLogs kgLogs = kgLogRepository
                .findTopByUserIdOrderByLoggedAtDesc(user.getId())
                .orElseThrow(() -> new RuntimeException("No weight"));
            userDTO.setKg(kgLogs.getKg());

        return userDTO;
    }

    @Override
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    @Override
    public UserDTO findUserByUsername(String username){
        Optional<User> resultUser = userRepository.findByUsername(username);
        UserDTO userDTO = new UserDTO();
        resultUser.ifPresent(user ->{
            userDTO.setUsername(user.getUsername());
            userDTO.setBornDate(user.getAdditionalInfo().getBornDate());
            userDTO.setFirstName(user.getAdditionalInfo().getFirstName());
            userDTO.setLastName(user.getAdditionalInfo().getLastName());
            userDTO.setGender(user.getAdditionalInfo().isGender());
        });
            return userDTO;
    }

    @Transactional
    @Override
    public UserDTO updateUser(UUID UUID, UserDTO userUpdateDTO) {
        System.out.println(userUpdateDTO);
        boolean updateData = false;
        User user = this.userRepository.findByUuid(UUID).orElseThrow();

        if (!userUpdateDTO.getFirstName().equals(user.getAdditionalInfo().getFirstName())) {
            updateData = true;
            user.getAdditionalInfo().setFirstName(userUpdateDTO.getFirstName());
        }
        if (!userUpdateDTO.getLastName().equals(user.getAdditionalInfo().getLastName())) {
            updateData = true;
            user.getAdditionalInfo().setLastName(userUpdateDTO.getLastName());
        }

        KGLogs kgLogs = this.kgLogRepository.findTopByUserIdOrderByLoggedAtDesc(user.getId()).orElseThrow();

        if (Double.compare(kgLogs.getKg(), userUpdateDTO.getKg()) != 0) {
            kgLogs.setKg(userUpdateDTO.getKg());
            kgLogs.setUser(user);
            kgLogs.setLoggedAt(LocalDateTime.now());
            this.kgLogRepository.save(kgLogs);
        }

        UserDTO userDto = new UserDTO();
        userDto.setId(user.getUuid());
        userDto.setFirstName(user.getAdditionalInfo().getFirstName());
        userDto.setLastName(user.getAdditionalInfo().getLastName());
        userDto.setKg(kgLogs.getKg());
        userDto.setCm(user.getAdditionalInfo().getCm());
        userDto.setBornDate(user.getAdditionalInfo().getBornDate());
        if (updateData) {
            this.userRepository.save(user);
        }
        return userDto;
    }
}
