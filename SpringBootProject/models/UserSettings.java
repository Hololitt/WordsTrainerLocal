package com.hololitt.SpringBootProject.models;

import com.hololitt.SpringBootProject.enums.FlashCardsTrainingVariety;
import com.hololitt.SpringBootProject.enums.LanguageCardsWritingEnabled;
import com.hololitt.SpringBootProject.enums.TrainingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "user_settings")
@Data
@AllArgsConstructor
@Builder
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private long userId;
    @OneToOne(mappedBy = "userSettings")
    private User user;

    @Column(name = "correct_answers_count_to_finish")
    private int correctAnswersCountToFinish;
    @Column(name = "translation_request_variety")
    private TrainingType translationRequestVariety;
    @Column(name = "count_language_cards_to_repeat")
    private int countLanguageCardsToRepeat;
    @Column(name = "flash_cards_training_variety")
    private FlashCardsTrainingVariety flashCardsTrainingVariety;
    @Column(name = "language_cards_writing_enabled")
    private LanguageCardsWritingEnabled languageCardsWritingEnabled;
}
