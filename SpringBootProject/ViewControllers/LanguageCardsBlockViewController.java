package com.hololitt.SpringBootProject.ViewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Home/languageCards/blocks")
public class LanguageCardsBlockViewController {
    @GetMapping
    public String showLanguageCardsBlockPage(){
        return "languageCardsBlock";
    }
}
