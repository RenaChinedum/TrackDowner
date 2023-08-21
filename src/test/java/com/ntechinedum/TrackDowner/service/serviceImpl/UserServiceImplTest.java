package com.ntechinedum.TrackDowner.service.serviceImpl;

import com.ntechinedum.TrackDowner.entity.User;
import com.ntechinedum.TrackDowner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void registerUser() {
        User user = new User();
                int userID = 1;
                user.setFirstName("Joe");
                user.setLastName("uche");
                user.setEmail("uche@gmail.com");
                user.setPassword("1234");
             when(userRepository.save(user)).thenReturn(user);
    }

    @Test
    void getUserByEmail() {
        User user = new User();
        user.setEmail("ntechinedu@gmail.com");
        when(userRepository.findByEmail("ntechinedu@gmail.com")).thenReturn(user);

    }

    @Test
    void getUserByPassword() {
        User user = new User();
        user.setPassword("1234");
        when(userRepository.findByPassword("1234")).thenReturn(user);
    }
}