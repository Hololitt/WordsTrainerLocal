package com.hololitt.SpringBootProject.repositories;

import com.hololitt.SpringBootProject.models.LanguageCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LanguageCardRepository extends JpaRepository<LanguageCard, Long> {
    List<LanguageCard> findAll();
    boolean existsByWordAndTranslation(String word, String translation);
    @Query("SELECT DISTINCT lc FROM LanguageCard lc WHERE lc.userId = :userId")
    List<LanguageCard> findByUserId(@Param("userId") int userId);

    @Query("SELECT lc FROM LanguageCard lc WHERE lc.isLastLearned = true AND lc.userId = :userId")
    List<LanguageCard> findLastLearnedCards(@Param("userId") int userId);

    @Query("SELECT SUM(lc.mistakesCount) FROM LanguageCard lc WHERE lc.userId = :userId")
    Integer calculateTotalMistakesCount(@Param("userId") int userId);

    @Query("SELECT COUNT(lc) FROM LanguageCard lc WHERE lc.userId = :userId")
        Integer calculateLanguageCards(@Param("userId") int userId);
    LanguageCard findByWordAndUserId(String word, int userId);
    LanguageCard findByTranslationAndUserId(String translation, int userId);
    long countByUserId(long userId);
    void deleteByIdAndUserId(int id, long userId);
    LanguageCard findByIdAndUserId(int id, long userId);
}
