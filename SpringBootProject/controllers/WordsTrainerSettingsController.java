package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.models.UserSettings;
import com.hololitt.SpringBootProject.models.WordsTrainerSettings;
import com.hololitt.SpringBootProject.services.UserService;
import com.hololitt.SpringBootProject.services.UserSettingsService;
import com.hololitt.SpringBootProject.services.WordsTrainerSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Home/WordsTrainer")
@RequiredArgsConstructor
public class WordsTrainerSettingsController {
    private final WordsTrainerSettingsService wordsTrainerSettingsService;
    private final UserService userService;
    private final UserSettingsService userSettingsService;


    @PostMapping("/submitSettings")
    public String submitSettings(@ModelAttribute("wordsTrainerSettings") WordsTrainerSettings wordsTrainerSettings,
                                 Model model){
        wordsTrainerSettings.setUserId(userService.getUserId());
        wordsTrainerSettingsService.setAllSettings(wordsTrainerSettings);

        userSettingsService.updateUserSettings(UserSettings.builder()
                .userId(userService.getUserId())
                .countLanguageCardsToRepeat(wordsTrainerSettings.getCountLanguageCardsToRepeat())
                .correctAnswersCountToFinish(wordsTrainerSettings.getCorrectAnswersCountToFinish())
                .flashCardsTrainingVariety(wordsTrainerSettings.getFlashCardsTrainingVariety())
                .languageCardsWritingEnabled(wordsTrainerSettings.getLanguageCardsWritingEnabled())
                .translationRequestVariety(wordsTrainerSettings.getTranslationRequestVariety())
                .build(), userService.getUserId());

        model.addAttribute("successfulSetting", "Your settings was successful saved!");
        return "wordsTrainerSettings";
    }
    @GetMapping("/settings")
    public String showSettings(Model model){
        model.addAttribute("wordsTrainerSettings", new WordsTrainerSettings());
        return "wordsTrainerSettings";
    }
}
