package com.hololitt.SpringBootProject.repositorys;

import com.hololitt.SpringBootProject.models.LanguageCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LanguageCardRepository extends JpaRepository<LanguageCard, Long> {
    List<LanguageCard> findAll();
    boolean existsByWordAndTranslation(String word, String translation);
    List<LanguageCard> findByUserId(int userId);
    LanguageCard findByWordAndUserId(String word, int userId);
    LanguageCard findByTranslationAndUserId(String translation, int userId);
    long countByUserId(long userId);
    void deleteByIdAndUserId(int id, long userId);
    LanguageCard findByIdAndUserId(int id, long userId);

}
