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
import java.util.Objects;
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

    @Transactional
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
        boolean updateData = false;
        User user = this.userRepository.findByUuid(UUID).orElseThrow();
        AdditionalInfo additionalInfo = user.getAdditionalInfo();

        String firstName = normalizeProfileName(userUpdateDTO.getFirstName());
        String lastName = normalizeProfileName(userUpdateDTO.getLastName());

        if (!Objects.equals(firstName, additionalInfo.getFirstName())) {
            updateData = true;
            additionalInfo.setFirstName(firstName);
        }
        if (!Objects.equals(lastName, additionalInfo.getLastName())) {
            updateData = true;
            additionalInfo.setLastName(lastName);
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
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(additionalInfo.getFirstName());
        userDto.setLastName(additionalInfo.getLastName());
        userDto.setKg(kgLogs.getKg());
        userDto.setCm(additionalInfo.getCm());
        userDto.setBornDate(additionalInfo.getBornDate());
        userDto.setGender(additionalInfo.isGender());
        if (updateData) {
            this.userRepository.save(user);
        }
        return userDto;
    }

    private String normalizeProfileName(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
