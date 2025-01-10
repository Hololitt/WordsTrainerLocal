package com.hololitt.SpringBootProject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "last_learned_language_cards")
@Data
public class LastLearnedLanguageCard {
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
    @Column(name = "user_id")
    private int userId;

    @Column(name = "mistake_count")
    private int mistakesCount;
    @Column(name = "repeatCount")
    private int repeatCount;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "last_repetition_date")
    private LocalDateTime lastRepetition;
}
