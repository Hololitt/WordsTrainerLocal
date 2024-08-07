package com.hololitt.SpringBootProject.models;

public class LanguageCardCreationForm {
    public LanguageCardCreationForm(){}
    private String word;
    private String translation;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
