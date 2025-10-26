package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.models.User;
import com.hololitt.SpringBootProject.models.UserStats;
import com.hololitt.SpringBootProject.services.UserService;
import com.hololitt.SpringBootProject.services.UserStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Home")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    private final UserStatsService userStatsService;

    @GetMapping("/userStats")
    public ResponseEntity<UserStats> calculateStats(){
        User currentUser = userService.getCurrentUser();

        UserStats userStats = calculateUserStats((int) currentUser.getId());

return ResponseEntity.ok(userStats);
    }

    private UserStats calculateUserStats(int userId){
        Integer totalMistakesCount = userStatsService.calculateTotalMistakesCount(userId);
        Integer languageCardsCount = userStatsService.calculateLanguageCardsCount(userId);

                return UserStats.builder()
                        .totalMistakesCount(totalMistakesCount)
                        .countLanguageCards(languageCardsCount)
                        .build();
    }
}
