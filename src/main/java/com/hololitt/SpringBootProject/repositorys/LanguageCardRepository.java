package com.hololitt.SpringBootProject.repositorys;

import com.hololitt.SpringBootProject.models.LanguageCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LanguageCardRepository extends CrudRepository<LanguageCard, Long> {
List<LanguageCard> findAll();
boolean existsByWordAndTranslation(String word, String translation);
List<LanguageCard> findByUserId(int userId);
LanguageCard findByWordAndUserId(String word, int userId);
LanguageCard findByTranslationAndUserId(String translation, int userId);
long countByUserId(long userId);
void deleteByUserIdAndWordAndTranslation(int userId, String word, String translation);
void deleteByIdAndUserId(int id, long userId);
boolean existsByIdAndUserId(int id, long userId);
    LanguageCard findByIdAndUserId(int id, long userId);

}
