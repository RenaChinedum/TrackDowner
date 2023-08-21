package com.ntechinedum.TrackDowner.controller;

import com.ntechinedum.TrackDowner.dto.TaskDTO;
import com.ntechinedum.TrackDowner.dto.UserDTO;
import com.ntechinedum.TrackDowner.exception.CustomUserException;
import com.ntechinedum.TrackDowner.service.serviceImpl.TaskServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class TaskController {
    private final TaskServiceImpl taskService;

    @Autowired
    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/task_form")
    public String taskForm(Model model) {
        TaskDTO task = new TaskDTO();
        model.addAttribute("task", task);
        return "activity_dashboard";
    }
    @GetMapping("/task")
    public String task(Model model, HttpSession httpSession) {
        TaskDTO task = (TaskDTO) httpSession.getAttribute("task");
        System.out.println(task);
        model.addAttribute("task", task);
        return "view_activity";
    }

    @PostMapping("/create_task")
    public String createTask( @ModelAttribute("task")TaskDTO taskDTO, BindingResult bindingResult,
                             HttpSession httpSession) throws CustomUserException {
        System.out.println(httpSession.getAttribute("userDTO"));
        if(bindingResult.hasErrors()) {
            return "activity_dashboard";
        }

        System.out.println((Long) httpSession.getAttribute("id"));
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        taskService.saveTask(taskDTO, (Long) httpSession.getAttribute("id"));
        return "redirect:/all_task";
    }

    @PostMapping("/edit_task/{taskId}")
    public String editTask( @ModelAttribute("task")TaskDTO taskDTO, BindingResult bindingResult,
                            HttpSession httpSession, @PathVariable("taskId") Long taskId) throws CustomUserException {
        if(bindingResult.hasErrors()) {
            return "edittask";
        }
        Long userDTO = (Long) httpSession.getAttribute("id");
        List<TaskDTO> tasks = taskService.getAllTask(userDTO);
        taskService.editTask(taskDTO, taskId);
//
        httpSession.setAttribute("task", tasks);


        return "redirect:/all_task";

    }
    @GetMapping("/edit_task_form/{taskId}")
    public String editTaskForm(Model model, @PathVariable("taskId") Long taskId) throws CustomUserException {
        TaskDTO taskDTO = taskService.getTaskById(taskId);

        model.addAttribute("task", taskDTO);
        model.addAttribute("currentTaskId", taskId);
        return "edittask";
    }

    @GetMapping("/view_activity/{taskId}")
    public String viewTask(@ModelAttribute("task") TaskDTO taskDTO, @PathVariable Long taskId,
                           HttpSession httpSession, Model model) throws CustomUserException {
        if(taskId == null) {
            throw new CustomUserException("Invalid Id");
        }
        TaskDTO task = taskService.getTaskById(taskId);
        httpSession.setAttribute("task", task);
        model.addAttribute("task",task);
        return "view_activity";
    }



    @GetMapping("/task/move_task/{taskId}")
    public String moveTask(HttpSession httpSession, @PathVariable Long taskId) throws CustomUserException, ExecutionException, InterruptedException {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");

        taskService.moveTask(taskId);

        List<TaskDTO> tasks = taskService.getAllTask(userDTO.getId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/all_task";
    }

    @GetMapping("/task/move_back/{taskId}")
    public String moveBack(HttpSession httpSession, @PathVariable Long taskId) throws CustomUserException, ExecutionException, InterruptedException {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");

        taskService.moveBack(taskId);

        List<TaskDTO> tasks = taskService.getAllTask(userDTO.getId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/all_task";
    }

    @GetMapping("/task/delete/{taskId}")
    public String deleteTask(HttpSession httpSession, @PathVariable Long taskId) throws CustomUserException, ExecutionException, InterruptedException {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");

        taskService.deleteTask(taskId);

        List<TaskDTO> tasks = taskService.getAllTask(userDTO.getId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/all_task";
    }


    @GetMapping("/all_task")
    public String allTask(HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks =  taskService.getAllTask(userDTO.getId());
//        httpSession.setAttribute("task", tasks);
        return "redirect:/listTasks";
    }
    @GetMapping("/task/pending")
    public String viewPendingTask(HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks = taskService.getTasksByPendingStatus(userDTO.getId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/listTasks";
    }

    @GetMapping("/task/in_progress")
    public String viewTaskInProgress(HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks = taskService.getTasksInProgressStatus(userDTO.getId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/listTasks";
    }
    @GetMapping("/task/completed")
    public String viewCompletedTask(HttpSession httpSession){
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
        List<TaskDTO> tasks = taskService.getAllCompletedTask(userDTO.getId());
        httpSession.setAttribute("task", tasks);

        return "redirect:/listTasks";
    }

    @GetMapping("/go_back")
    public String goBack() {

        return "redirect:/all_task";
    }
    @GetMapping("/search")
    public String searchTask( @ModelAttribute("task")TaskDTO taskDTO, BindingResult bindingResult,
                             HttpSession httpSession) {
        if (httpSession.getAttribute("id")==null) {
            return "redirect:login";
        }else {
            System.out.println(taskDTO.getTitle());
            UserDTO userDTO = (UserDTO) httpSession.getAttribute("userDTO");
            System.out.println(userDTO.getId());

            List<TaskDTO> tasks = taskService.searchTask(taskDTO.getTitle(), userDTO.getId());
//        log.info("Printing task");
            httpSession.setAttribute("task", tasks);
            return "redirect:/listTasks";
        }
    }
    @GetMapping("/listTasks")
    public String listTasks(
            Model model, HttpSession httpSession,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {

        if (httpSession.getAttribute("id")==null){
            return "redirect:login";
        }else {

            int currentPage = page.orElse(1);
            int pageSize = size.orElse(5);


            List<TaskDTO> tasks = taskService.getAllTask((Long) httpSession.getAttribute("id"));
            System.out.println(tasks);
            TaskDTO task = new TaskDTO();

            Page<TaskDTO> taskPage = taskService.findPaginated(tasks, PageRequest.of(currentPage - 1, pageSize));

            model.addAttribute("taskPage", taskPage);

            System.out.println(taskPage);

            int totalPages = taskPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }


            System.out.println(httpSession.getAttribute("id"));


            UserDTO currentUser = (UserDTO) httpSession.getAttribute("userDTO");
            model.addAttribute("task", task);
            model.addAttribute("user", new UserDTO());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("tasks", tasks);
            return "index";
        }
    }
}
