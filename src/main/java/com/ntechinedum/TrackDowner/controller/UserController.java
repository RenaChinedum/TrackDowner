package com.ntechinedum.TrackDowner.controller;

import com.ntechinedum.TrackDowner.dto.TaskDTO;
import com.ntechinedum.TrackDowner.dto.UserDTO;
import com.ntechinedum.TrackDowner.entity.User;
import com.ntechinedum.TrackDowner.exception.CustomUserException;
import com.ntechinedum.TrackDowner.service.serviceImpl.TaskServiceImpl;
import com.ntechinedum.TrackDowner.service.serviceImpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {
    private final UserServiceImpl  userService;
    private final TaskServiceImpl taskService;

    public UserController(UserServiceImpl userService, TaskServiceImpl taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping( "/")
    public String renderHome(Model model){
        model.addAttribute( "user", new UserDTO());
        return "login";
    }

    @GetMapping("/register")
    public String renderSignup(Model model){
        model.addAttribute("user", new UserDTO());
        return "register";

    }
    @PostMapping("/register")
    public String registerUser( @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult,
                               Model model) throws CustomUserException {
        User existingUser = userService.getUserByEmail(userDTO.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            bindingResult.rejectValue("email", "",
                    "There is already an account registered with the same email");
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "/register";
        }

        userService.registerUser(userDTO);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String loginUser( @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult,
                            HttpServletRequest request) throws CustomUserException {
        HttpSession session = request.getSession();

        if(bindingResult.hasErrors()) {
            return "redirect:/login";
        }

        UserDTO uD = userService.loginUser(userDTO);
        if(userDTO == null) {
            bindingResult.rejectValue("userName", "", "Couldn't find a user with the email");
            return "redirect:/login";
        }

        System.out.println(uD.getId());
        session.setAttribute("id", uD.getId());
        session.setAttribute("userDTO", userDTO);
        return "redirect:/listTasks";
    }

    @GetMapping("/login")
    public String renderLogin(Model model){
        model.addAttribute("user", new UserDTO());
        return "login";
    }



    @GetMapping("/home")
    public String displayHome(Model model, HttpSession httpSession) {
        List<TaskDTO> tasks = (List<TaskDTO>) httpSession.getAttribute("task");
        UserDTO currentUser = (UserDTO) httpSession.getAttribute("userDTO");
        model.addAttribute("user", new UserDTO());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("tasks", tasks);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession userSession = request.getSession();
        userSession.invalidate();
        return "redirect:/";
    }
}
