package com.hololitt.SpringBootProject.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageCardCreationForm {
    private String word;
    private String translation;
}
