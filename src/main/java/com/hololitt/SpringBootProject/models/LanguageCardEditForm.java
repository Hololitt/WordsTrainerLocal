package com.hololitt.SpringBootProject.models;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
