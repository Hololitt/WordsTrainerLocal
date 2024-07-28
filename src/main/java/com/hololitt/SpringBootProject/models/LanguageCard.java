package com.hololitt.SpringBootProject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
@Entity
@Table(name = "LanguageCards")
public class LanguageCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "word")
    @NotEmpty(message = "Write the word")
    private String word;
    @Column(name = "translation")
    @NotEmpty(message = "Write the translation")
    private String translation;
    @Column(name = "userId")
private int userId;

    @Column(name = "mistakeCount")
    private int mistakesCount;
    @Column(name = "repeatCount")
    private int repeatCount;
    public LanguageCard() {}
    public LanguageCard(String word, String translation, long userId, int id){
        this.word = word;
        this.translation = translation;
        this.userId = (int) userId;
        this.id = id;
    }
    public LanguageCard(String word, String translation){
        this.word = word;
        this.translation = translation;
    }
    public LanguageCard(String word, String translation, int mistakesCount, int repeatCount){
        this.word = word;
        this.translation = translation;
        this.mistakesCount = mistakesCount;
        this.repeatCount = repeatCount;
    }
    public void setUserId(long userId){
        this.userId = (int) userId;
    }
    public int getUserId(){
        return userId;
    }
    public int getId(){
        return id;
    }
    public String getWord() {
        return word;
    }
    public int getMistakesCount(){
        return mistakesCount;
    }
    public void incrementMistakesCount(){
        mistakesCount += 1;
    }
    public int getRepeatCount(){
        return repeatCount;
    }
    public void incrementRepeatCount(){
        repeatCount += 1;
    }
    public String getTranslation() {
        return translation;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

}
