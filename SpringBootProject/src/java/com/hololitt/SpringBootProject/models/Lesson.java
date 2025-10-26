package com.hololitt.SpringBootProject.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lessons")
@Data
public class Lesson {
    public Lesson(String title, String text){
        this.title = title;
        this.text = text;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

@ManyToMany
@JoinTable(
        name = "lessons_languageCards",
        joinColumns = @JoinColumn(name = "lesson_id"),
        inverseJoinColumns = @JoinColumn(name = "languageCard_id")
)
    private Set<LanguageCard> languageCards = new HashSet<>();

    @Column(name = "text", columnDefinition = "TEXT")
    @Lob
    private String text;

}
