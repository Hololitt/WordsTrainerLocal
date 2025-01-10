package com.hololitt.SpringBootProject.repositorys;

import com.hololitt.SpringBootProject.models.LastLearnedLanguageCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LastLearnedLanguageCardRepository extends CrudRepository<LastLearnedLanguageCard, Integer> {
    void deleteAllByUserId(int userId);
    List<LastLearnedLanguageCard> findAllByUserId(int userId);
}
