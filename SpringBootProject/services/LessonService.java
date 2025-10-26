package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.DTO.LessonDTO;
import com.hololitt.SpringBootProject.enums.OperationStatusRepository;
import com.hololitt.SpringBootProject.models.Lesson;
import com.hololitt.SpringBootProject.repositorys.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;

    public OperationStatusRepository saveLesson(Lesson lesson){
        try{
            lessonRepository.save(lesson);
            return OperationStatusRepository.OK;
        }catch(DataAccessException e){
            return OperationStatusRepository.UNSUCCESSFULSAVING;
        }
    }

    public LessonDTO findLesson(String title){
        Lesson lesson = lessonRepository.findByTitle(title);

        if(lesson == null){
           return LessonDTO.builder()
                    .operationStatus(OperationStatusRepository.NOT_FOUND)
                    .build();
        }
        return LessonDTO.builder()
                .lesson(lesson)
                .operationStatus(OperationStatusRepository.OK)
                .build();
    }
}
