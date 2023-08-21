package com.ntechinedum.TrackDowner.service.serviceImpl;

import com.ntechinedum.TrackDowner.repository.TaskRepository;
import com.ntechinedum.TrackDowner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;


class TaskServiceImplTest {
    @Mock
    private TaskServiceImpl taskService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTask() {

    }

    @Test
    void getAllTask() {
    }



    @Test
    void getTasksByPendingStatus() {
    }

    @Test
    void moveTask() {
    }
    @Test
    void getTasksInProgressStatus() {

    }

    @Test
    void getAllCompletedTask() {
    }

    @Test
    void moveBack() {
    }

    @Test
    void deleteTask() {
    }

    @Test
    void editTask() {
    }

    @Test
    void searchTask() {
    }

    @Test
    void findPaginated() {
    }
}