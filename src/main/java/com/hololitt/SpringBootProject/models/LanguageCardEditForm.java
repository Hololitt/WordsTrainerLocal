package com.hololitt.SpringBootProject.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LanguageCardEditForm {
    public LanguageCardEditForm(int id, String word, String translation) {
        this.id = id;
        this.word = word;
        this.translation = translation;
    }
   public LanguageCardEditForm(){}

    private int id;
    private String word;
    private String translation;

}
