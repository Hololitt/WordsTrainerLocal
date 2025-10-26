package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.DTO.LanguageCardNoteDTO;
import com.hololitt.SpringBootProject.enums.OperationStatus;
import com.hololitt.SpringBootProject.models.LanguageCardCreationForm;
import com.hololitt.SpringBootProject.models.LanguageCardNote;
import com.hololitt.SpringBootProject.services.LanguageCardNoteService;
import com.hololitt.SpringBootProject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/Home/WordsTrainer")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class LanguageCardNoteController {
    private final LanguageCardNoteService languageCardNoteService;
    private final UserService userService;

@GetMapping("/cardsNotes{userId}")
    public ResponseEntity<Set<LanguageCardNote>> getAllLanguageCardNotesByUser(@PathVariable long userId){
    LanguageCardNoteDTO languageCardNoteDTO = languageCardNoteService.findAllByUserId(userId);
    OperationStatus operationStatus = languageCardNoteDTO.getOperationStatus();

    return switch(operationStatus){
        case OK -> ResponseEntity.ok(languageCardNoteDTO.getLanguageCardNoteSet());

        case NOT_FOUND -> ResponseEntity.notFound().build();

        default -> ResponseEntity.internalServerError().build();
    };
}

@PostMapping("/createCardsNotes")
    public ResponseEntity<HttpStatus> saveCardsNotes(@RequestParam Set<LanguageCardCreationForm> languageCardForms){
    Set<LanguageCardNote> languageCardNotes = new HashSet<>();
    languageCardForms.forEach(lc -> {
        LanguageCardNote languageCardNote = new LanguageCardNote();

        languageCardNote.setWord(lc.getWord());
        languageCardNote.setTranslation(lc.getTranslation());
        languageCardNote.setUserId((int) userService.getUserId());

        languageCardNotes.add(languageCardNote);
    });

    LanguageCardNoteDTO languageCardNoteDTO = languageCardNoteService.save(languageCardNotes);
    OperationStatus operationStatus = languageCardNoteDTO.getOperationStatus();

    return switch(operationStatus){
        case OK -> ResponseEntity.ok().build();
        case INVALID_INPUT -> ResponseEntity.badRequest().build();
        default -> ResponseEntity.internalServerError().build();
    };
}
}
