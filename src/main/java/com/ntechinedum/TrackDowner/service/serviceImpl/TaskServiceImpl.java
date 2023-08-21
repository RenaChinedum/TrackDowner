package com.ntechinedum.TrackDowner.service.serviceImpl;

import com.ntechinedum.TrackDowner.dto.TaskDTO;
import com.ntechinedum.TrackDowner.entity.Tasks;
import com.ntechinedum.TrackDowner.entity.User;
import com.ntechinedum.TrackDowner.enums.Status;
import com.ntechinedum.TrackDowner.exception.CustomUserException;
import com.ntechinedum.TrackDowner.repository.TaskRepository;
import com.ntechinedum.TrackDowner.repository.UserRepository;
import com.ntechinedum.TrackDowner.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveTask(TaskDTO taskDTO, Long UserID) throws CustomUserException {
        Optional<User> userOptional = userRepository.findById(UserID);
        User user;
        if(userOptional.isPresent()){
            user = userOptional.get();
        }else {
            throw new CustomUserException("User not found");
        }
        Tasks task = Tasks.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .build();
        task.setCompletedAt(task.getCompletedAt());
        task.setStatus(Status.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        task.setUser(user);

        taskRepository.save(task);

    }

    @Override
    public List<TaskDTO> getAllTask(Long userID) {
        List<Tasks> tasks = taskRepository.findAllByUserId(userID);
        System.out.println(tasks.size());

        return tasks.stream().map(this::mappedToDTO).toList();
    }

    private TaskDTO mappedToDTO(Tasks task) {
        TaskDTO taskDTO = TaskDTO.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
        taskDTO.setCreatedAt(task.getCreatedAt());
        taskDTO.setUpdatedAt(task.getUpdatedAt());
        taskDTO.setCompletedAt(task.getCompletedAt());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setId(task.getTaskId());

        return taskDTO;
    }

    @Override
    public TaskDTO getTaskById(Long id) throws CustomUserException {
        Optional<Tasks> tasks = taskRepository.findById(id);
        if(tasks.isPresent()){
            return mappedToDTO(tasks.get());
        }else {
            throw  new CustomUserException( "Task is Empty");
        }
    }

    @Override
    public List<TaskDTO> getTasksByPendingStatus(Long userID) {
        List<Tasks> tasks = taskRepository.findAllByStatusPending(userID);
        return tasks.stream().map(this::mappedToDTO).toList();
    }

    @Override
    public void moveTask(Long id) throws CustomUserException {
        Tasks tasks = taskRepository.findById(id).get();
        System.out.println("Task is " + tasks);
        if(tasks.getStatus() == Status.DONE){
            throw new CustomUserException("Task has been completed and cannot be moved further. " +
                    "You can consider moving it back");
        }
        taskRepository.moveTask(id);

        taskRepository.updateUpdateAt(LocalDateTime.now(), id);

        if(tasks.getStatus() == Status.IN_PROGRESS){
            taskRepository.updateCompletedAt(LocalDateTime.now(), id);
        }
    }

    @Override
    public List<TaskDTO> getTasksInProgressStatus(Long userId) {
        List<Tasks> tasks = taskRepository.findAllInProgress(userId);
        return tasks.stream().map(this::mappedToDTO).toList();
    }

    @Override
    public List<TaskDTO> getAllCompletedTask(Long userID) {
        List<Tasks> tasks = taskRepository.findAllDone(userID);
        return tasks.stream().map(this::mappedToDTO).toList();
    }

    @Override
    public void moveBack(Long id) throws CustomUserException {
      Tasks tasks = taskRepository.findById(id).get();
        if(tasks.getStatus() == Status.PENDING){
            throw new CustomUserException("Task cannot be moved further. " +
                    "You can consider moving it up");
        }
        taskRepository.moveBack(id);

        taskRepository.updateUpdateAt(LocalDateTime.now(), id);

        if(tasks.getStatus() == Status.DONE) {
            taskRepository.removeCompletedAt(id);
        }
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public void editTask(TaskDTO taskDTO, Long id) {
        try {
            taskRepository.updateTask(taskDTO.getTitle(), taskDTO.getDescription(), id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        taskRepository.updateUpdateAt(LocalDateTime.now(), id);
    }

//    @Override
//    public void editTask(TaskDTO taskDTO, long id){
//
//    }

    @Override
    public List<TaskDTO> searchTask(String title, Long userId) {
        List<Tasks> tasks = taskRepository.findAllByUserId(userId);
        List<Tasks> searhchedTasks;

        searhchedTasks = tasks.stream().filter(task-> task.getTitle().toLowerCase().
                contains(title.toLowerCase())).collect(Collectors.toList());

        return searhchedTasks.stream().map(this::mappedToDTO).collect(Collectors.toList());
    }
    public Page<TaskDTO> findPaginated(List<TaskDTO> tasks, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<TaskDTO> list;

        if (tasks.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, tasks.size());
            list = tasks.subList(startItem, toIndex);
        }

        return new PageImpl<TaskDTO>(list, PageRequest.of(currentPage, pageSize), tasks.size());
    }
}
