package com.hololitt.SpringBootProject.DTO;

import com.hololitt.SpringBootProject.enums.OperationStatusRepository;
import com.hololitt.SpringBootProject.models.Lesson;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LessonDTO {
    private OperationStatusRepository operationStatus;
    private Lesson lesson;
}
