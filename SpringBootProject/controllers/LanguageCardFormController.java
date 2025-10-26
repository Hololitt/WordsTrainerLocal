package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.models.LanguageCardCreationForm;
import com.hololitt.SpringBootProject.models.LanguageCardFormUpdateRequest;
import com.hololitt.SpringBootProject.services.LanguageCardCacheService;
import com.hololitt.SpringBootProject.services.LanguageCardService;
import com.hololitt.SpringBootProject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Home/WordsTrainer")
@RequiredArgsConstructor
public class LanguageCardFormController {
    private final LanguageCardService languageCardService;
    private final LanguageCardCacheService languageCardCacheService;
    private final UserService userService;
    @PostMapping("/updateCard")
    public ResponseEntity<HttpStatus> updateLanguageCard(@RequestBody LanguageCardFormUpdateRequest request) {
        LanguageCardCreationForm updatedForm = request.getUpdatedForm();
        LanguageCardCreationForm oldForm = request.getOldForm();

        if (languageCardService.isLanguageCardExists(updatedForm.getWord(), updatedForm.getTranslation())) {
            return ResponseEntity.badRequest().build();
        }

        languageCardCacheService.updateCreatedLanguageCard(oldForm, updatedForm, userService.getUserId());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
