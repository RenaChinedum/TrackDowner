package com.ntechinedum.TrackDowner.service;

import com.ntechinedum.TrackDowner.dto.UserDTO;
import com.ntechinedum.TrackDowner.entity.User;

public interface UserService {
    void registerUser(UserDTO userDTO);

    UserDTO loginUser(UserDTO userDTO);

    User getUserByEmail(String email);

    User getUserByPassword(String password);
}
