package com.strongBeton.strongBeton.service.user;

import com.strongBeton.strongBeton.dto.user.UserDTO;
import com.strongBeton.strongBeton.dao.*;
import com.strongBeton.strongBeton.entity.user.User;
import com.strongBeton.strongBeton.entity.coach.UserTrainingDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.strongBeton.strongBeton.enums.PhotoType.PROFILE;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserTrainingDetailsRepository userTrainingDetails;
    private final CloudPhotoRepository photoRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserTrainingDetailsRepository userTrainingDetails,
                           CloudPhotoRepository photoRepository) {
        this.userRepository = userRepository;
        this.userTrainingDetails = userTrainingDetails;
        this.photoRepository = photoRepository;
    }

    public UserDTO loadUserDataByEmail(String email){
            User user = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("No such user"));
            Optional<UserTrainingDetails> userTraningDataExist = userTrainingDetails.findById(user.getId());
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getUuid());
            userDTO.setUsername(user.getUsername());
            userDTO.setFirstName(user.getAdditionalInfo().getFirstName());
            userDTO.setLastName(user.getAdditionalInfo().getLastName());
            userDTO.setBorn_date(user.getAdditionalInfo().getBornDate());
            userDTO.setKg(user.getAdditionalInfo().getKg());
            userDTO.setCm(user.getAdditionalInfo().getCm());
            userDTO.setCity(user.getAdditionalInfo().getCity().getCityName());
            userDTO.setGender(user.getAdditionalInfo().isGender());
            photoRepository.findByUserIdAndPhotoType(user.getId(), PROFILE.toString())
                    .ifPresent(photo -> userDTO.setProfilePhotoUrl(photo.getPhotoUrl()));



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
            userDTO.setCity(user.getAdditionalInfo().getCity().getCityName());
            userDTO.setBorn_date(user.getAdditionalInfo().getBornDate());
            userDTO.setFirstName(user.getAdditionalInfo().getFirstName());
            userDTO.setLastName(user.getAdditionalInfo().getLastName());
            userDTO.setGender(user.getAdditionalInfo().isGender());
        });
            return userDTO;
    }
}
