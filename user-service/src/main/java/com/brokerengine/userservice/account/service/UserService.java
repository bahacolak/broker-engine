package com.brokerengine.userservice.account.service;


import com.brokerengine.userservice.account.config.JwtService;
import com.brokerengine.userservice.account.config.PasswordEncoderService;
import com.brokerengine.userservice.account.entity.Role;
import com.brokerengine.userservice.account.entity.User;
import com.brokerengine.userservice.account.repository.UserRepository;
import com.brokerengine.userservice.account.web.advice.exception.UserConflictException;
import com.brokerengine.userservice.account.web.advice.exception.UserNotFoundException;
import com.brokerengine.userservice.account.web.dto.UserDto;
import com.brokerengine.userservice.account.web.request.AuthenticationRequest;
import com.brokerengine.userservice.account.web.request.RegisterRequest;
import com.brokerengine.userservice.account.web.request.UpdateUserRequest;
import com.brokerengine.userservice.account.web.response.AuthenticationResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoderService passwordEncoderService;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoderService = passwordEncoderService;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : users){
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDto(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName()))
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (doesUserExist(request.getEmail())) {
            throw new UserConflictException("User with email: " + request.getEmail() + " already exists!");
        }

        String salt = passwordEncoderService.generateSalt();
        String hashedPassword = passwordEncoderService.hash(request.getPassword(), salt);

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(hashedPassword)
                .salt(salt)
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email"));

        var passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(request.getPassword() + user.getSalt(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private boolean doesUserExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto updateUser(Long id, UpdateUserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setEmail(request.getEmail());

        userRepository.save(existingUser);

        return new UserDto(
                existingUser.getId(),
                existingUser.getFirstName(),
                existingUser.getLastName(),
                existingUser.getEmail()
        );
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}

