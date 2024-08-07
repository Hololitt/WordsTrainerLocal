package com.hololitt.SpringBootProject.repositorys;

import com.hololitt.SpringBootProject.models.LanguageCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LanguageCardRepository extends CrudRepository<LanguageCard, Long> {
List<LanguageCard> findAll();
List<LanguageCard> findByUserId(int userId);
LanguageCard findByWord(String word);
LanguageCard findByTranslation(String translation);
long countByUserId(long userId);

void deleteByUserIdAndWord(int userId, String word);
}
