package com.hololitt.SpringBootProject.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LanguageCardCreationForm {
    public LanguageCardCreationForm(){}
    private String word;
    private String translation;
}
