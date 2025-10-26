package com.hololitt.SpringBootProject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "language_card_note")
@Data
public class LanguageCardNote {
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
}
