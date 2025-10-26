package com.hololitt.SpringBootProject.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class LessonContext implements Serializable {
    private final Integer userId;
    private int progress;

}
