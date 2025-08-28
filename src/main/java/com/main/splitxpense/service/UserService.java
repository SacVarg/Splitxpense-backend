package com.main.splitxpense.service;

import com.main.splitxpense.dto.UserRequest;
import com.main.splitxpense.dto.UserResponse;
import com.main.splitxpense.exception.DuplicateEmailException;
import com.main.splitxpense.exception.UserNotFoundException;
import com.main.splitxpense.mapper.DTOMapper;
import com.main.splitxpense.model.User;
import com.main.splitxpense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DTOMapper dtoMapper;

    public UserResponse createUser(UserRequest request) {
        // Check for duplicate email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException(request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        try {
            User savedUser = userRepository.save(user);
            return dtoMapper.toUserResponse(savedUser);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEmailException(request.getEmail());
        }
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return dtoMapper.toUserResponseList(users);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return dtoMapper.toUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return dtoMapper.toUserResponse(user);
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Check if email is being changed to an existing email
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException(request.getEmail());
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        try {
            User savedUser = userRepository.save(user);
            return dtoMapper.toUserResponse(savedUser);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEmailException(request.getEmail());
        }
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}
