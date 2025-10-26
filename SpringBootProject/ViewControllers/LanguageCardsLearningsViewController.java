package com.hololitt.SpringBootProject.ViewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Home/WordsTrainer/learningPage")
public class LanguageCardsLearningsViewController {
    @GetMapping
    public String showLearningPage(){
        return "learnLanguageCards";
    }
}
