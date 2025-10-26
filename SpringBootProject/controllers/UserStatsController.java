package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.DTO.UsersStatsDTO;
import com.hololitt.SpringBootProject.services.UserStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Home")
@RequiredArgsConstructor
public class UserStatsController {
    private final UserStatsService userStatsService;
    @GetMapping("/getUsersStats")
    public ResponseEntity<UsersStatsDTO> getUsersStats(){
        UsersStatsDTO userStatsDTO = userStatsService.getUsersStats();

        return ResponseEntity.ok(userStatsDTO);
    }
}
