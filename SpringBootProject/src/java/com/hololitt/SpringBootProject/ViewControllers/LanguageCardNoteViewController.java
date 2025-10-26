package com.hololitt.SpringBootProject.ViewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LanguageCardNoteViewController {

    @GetMapping("/Home/WordsTrainer/notes")
    public String showLanguageCardNotePage(){
        return "languageCardNote";
    }
}
