package com.hololitt.SpringBootProject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "LanguageCards")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LanguageCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "word")
    @NotNull
    @NotEmpty(message = "Write the word")
    private String word;

    @Column(name = "translation")
    @NotNull
    @NotEmpty(message = "Write the translation")
    private String translation;

    @Column(name = "user_id")
    @NotEmpty
    private int userId;

    @Column(name = "mistake_count")
    @NotNull
    private int mistakesCount;

    @Column(name = "repeat_count")
    @NotEmpty
    private int repeatCount;

    @Column(name = "creation_date", updatable = false, nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_repetition")
    private LocalDateTime lastRepetition;

    @Column(name = "is_last_learned")
    private Boolean isLastLearned;

    @ManyToMany(mappedBy = "languageCards")
    private Set<LanguageCardsBlock> languageCardsBlocks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToMany(mappedBy = "languageCards")
private Set<Lesson> lessons = new HashSet<>();


    @PrePersist
    protected void onCreation() {
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
