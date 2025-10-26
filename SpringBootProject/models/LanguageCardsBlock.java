package com.hololitt.SpringBootProject.models;

    import jakarta.persistence.*;
    import lombok.Data;

    import java.time.LocalDateTime;
    import java.time.ZoneId;
    import java.util.HashSet;
    import java.util.Set;
@Entity
@Table(name = "language_card_block")
@Data
    public class LanguageCardsBlock {
    public LanguageCardsBlock(String type, User user){
        this.type = type;
        this.user = user;
    }
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;
        @ManyToMany
        @JoinTable(
                name = "block_card",
                joinColumns = @JoinColumn(name = "block_id"),
                inverseJoinColumns = @JoinColumn(name = "card_id")
        )
        private Set<LanguageCard> languageCards = new HashSet<>();

        @Column(name = "creation_date", updatable = false, nullable = false)
        private LocalDateTime creationDate;

        @Column(name = "type")
        private String type;
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;
    @PrePersist
        public void initCreationDate(){
        creationDate = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
    }
    }