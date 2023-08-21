package com.ntechinedum.TrackDowner.service.serviceImpl;

import com.ntechinedum.TrackDowner.dto.UserDTO;
import com.ntechinedum.TrackDowner.entity.User;
import com.ntechinedum.TrackDowner.repository.UserRepository;
import com.ntechinedum.TrackDowner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(UserDTO userDTO) {
        User user = User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
        userRepository.save(user);
    }

    @Override
    public UserDTO loginUser(UserDTO userDTO) {
        User user = userRepository.findByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword());
        if(user == null) {
            return null;
        }
        return mappedToDTO(user);
    }

    private UserDTO mappedToDTO(User user) {
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        return userDTO;
    }

    @Override
    public User getUserByEmail(String email) {
            return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByPassword(String password) {
        return userRepository.findByPassword(password);
    }


}
