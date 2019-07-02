package com.social.user;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            modelMapper.map(user, userDTO);
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    public UserDTO getUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        log.info("Retrieving user " + username + " from database");
        if (userOptional.isPresent()) {
            log.info("User " + username + "found in database");
            ModelMapper modelMapper = new ModelMapper();
            UserDTO userDTO = new UserDTO();
            modelMapper.map(userOptional.get(), userDTO);
            return userDTO;
        } else {
            log.error("User " + username + " not found in database");
            return null;
        }
    }

    public boolean addUser(UserDTO userDTO) {
        //checking for username and email
        User user = new User();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (userRepository.countByEmailOrUsername(user.getEmail(), user.getUsername()) != 0) {
            log.error("Username or email is already used");
            return false;
        } else {
            userRepository.save(user);
            log.info("User added");
            return true;
        }

    }

    public boolean updateUser(UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findByUsername(userDTO.getUsername());
        if (userOptional.isPresent()) {
            log.info("User " + userDTO.getUsername() + "found in database");
            ModelMapper modelMapper = new ModelMapper();
            User user = userOptional.get();
            modelMapper.map(userDTO, user);
            userRepository.save(user);
            return true;

        } else {
            log.error("User " + userDTO.getUsername() + " not found in database");
            return false;
        }
    }

    public void deleteUser(String username) {
        log.info("Deleting user " + username);
        userRepository.deleteByUsername(username);
    }

    public void ActivateOrDeactivateUser(String username, boolean activate) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (activate) {
                user.setActive(true);
                log.info("Activating user");
            } else {
                user.setActive(false);
                log.info("Deactivating user");
            }
            userRepository.save(user);

        } else {
            log.error("User not found");
        }

    }
}
