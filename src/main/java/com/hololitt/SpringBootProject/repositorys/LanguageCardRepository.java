package com.hololitt.SpringBootProject.repositorys;

import com.hololitt.SpringBootProject.models.LanguageCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LanguageCardRepository extends CrudRepository<LanguageCard, Long> {
    List<LanguageCard> findAll();
    List<LanguageCard> findAllByUserId(int userId);
    boolean existsByWordAndTranslation(String word, String translation);
    List<LanguageCard> findByUserId(int userId);
    LanguageCard findByWordAndUserId(String word, int userId);
    LanguageCard findByTranslationAndUserId(String translation, int userId);
    long countByUserId(long userId);
    void deleteByIdAndUserId(int id, long userId);
    void deleteByUserId(int userId);
    LanguageCard findByIdAndUserId(int id, long userId);

}
