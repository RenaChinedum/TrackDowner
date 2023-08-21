package com.ntechinedum.TrackDowner.service;

import com.ntechinedum.TrackDowner.dto.TaskDTO;
import com.ntechinedum.TrackDowner.exception.CustomUserException;

import java.util.List;

public interface TaskService {
    void saveTask(TaskDTO taskDTO, Long UserId) throws CustomUserException;

    List<TaskDTO> getAllTask(Long userId);

    TaskDTO getTaskById(Long taskId) throws CustomUserException;

    List<TaskDTO> getTasksByPendingStatus(Long userId);

    void moveTask(Long taskId) throws CustomUserException;

    List<TaskDTO> getTasksInProgressStatus(Long userId);

    List<TaskDTO> getAllCompletedTask(Long userId);

    void moveBack(Long taskId) throws CustomUserException;

    void deleteTask(Long taskId);

    void editTask(TaskDTO taskDTO, Long taskId);


    List<TaskDTO> searchTask(String title, Long userId);
}
