package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.enums.OperationStatusRepository;
import com.hololitt.SpringBootProject.DTO.LessonDTO;
import com.hololitt.SpringBootProject.models.Lesson;
import com.hololitt.SpringBootProject.models.LessonRequest;
import com.hololitt.SpringBootProject.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/{title}")
    public ResponseEntity<?> findLesson(@PathVariable("title") String title){
        LessonDTO lessonDTO = lessonService.findLesson(title);

        OperationStatusRepository status = lessonDTO.getOperationStatus();
Lesson lesson = lessonDTO.getLesson();

        if(status.equals(OperationStatusRepository.OK)){
            return ResponseEntity.ok(lesson);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createLesson(@RequestBody LessonRequest lessonRequest){
        String title = lessonRequest.getTitle();
        String text = lessonRequest.getText();

        Lesson lesson = new Lesson(title, text);

        OperationStatusRepository status = lessonService.saveLesson(lesson);

        if(status.equals(OperationStatusRepository.OK)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
