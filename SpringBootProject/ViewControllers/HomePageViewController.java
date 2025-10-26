package com.hololitt.SpringBootProject.ViewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class HomePageViewController {

    @GetMapping
    public String showWordsTrainerPage(){
        return "WordsTrainer";
    }
}
