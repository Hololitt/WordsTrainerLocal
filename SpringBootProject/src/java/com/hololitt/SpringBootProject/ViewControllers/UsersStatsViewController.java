package com.hololitt.SpringBootProject.ViewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Home")
public class UsersStatsViewController {
    @GetMapping("/stats")
    public String showUsersStatsPage(){
        return "usersStats";
    }
}
