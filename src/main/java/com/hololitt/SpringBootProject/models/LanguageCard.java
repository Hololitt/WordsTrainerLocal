package com.hololitt.SpringBootProject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "LanguageCards")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_repetition")
    private LocalDateTime lastRepetition;

    @PrePersist
    protected void onCreation(){
        creationDate = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
    }


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

    public LanguageCard(String word, String translation, long userId) {
        this.word = word;
        this.translation = translation;
        this.userId = (int) userId;
    }
    public void incrementRepeatCount(){
        repeatCount += 1;
    }
    public void incrementMistakesCount(){
        mistakesCount += 1;
    }

}
