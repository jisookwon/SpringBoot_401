package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    CourseRepository courseRepository;

    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){
        model.addAttribute("user",user);
        if (result.hasErrors())
        {
            return "registration";
        }
        else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        return "list";
    }
//    @RequestMapping("/")
//    public String index() {
//        return "list";
//    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model){
        User myuser = ((CustomUserDetails)((UsernamePasswordAuthenticationToken) principal)
                .getPrincipal()).getUser();
        model.addAttribute("myuser", myuser);
        return "secure";
    }


    // =====From Course Project
    @RequestMapping("/")
    public String listCourses(Model model){
        model.addAttribute("courses", courseRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String courseForm(Model model){
        model.addAttribute("course", new Course());
        return "courseform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Course course, BindingResult result, Model model){
        model.addAttribute("course", new Course());

        if (result.hasErrors()){
            return "courseform";
        }
        courseRepository.save(course);
        //return "list";
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showCourses(@PathVariable("id") long id, Model model){
        model.addAttribute("course", courseRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateCourses(@PathVariable("id") long id, Model model){
        model.addAttribute("course", courseRepository.findById(id).get());
        return "courseform";
    }

    @RequestMapping("/delete/{id}")
    public String delCourses(@PathVariable("id") long id){
        courseRepository.deleteById(id);
        return "redirect:/";
       // return "list";
    }

}







