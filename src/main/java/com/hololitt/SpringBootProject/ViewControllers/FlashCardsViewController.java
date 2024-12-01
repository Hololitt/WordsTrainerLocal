package com.hololitt.SpringBootProject.ViewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Home/WordsTrainer")
public class FlashCardsViewController {
    @GetMapping("/flashcards/matching")
    public String showFlashCardsMatchingPage(){
        return "flashCardsMatching";
    }
    @GetMapping("/flashcards")
    public String showFlashCardsPage() {
        return "flashCards";
    }
}
