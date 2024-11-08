package com.hololitt.SpringBootProject.repositorys;

import com.hololitt.SpringBootProject.models.LastLearnedLanguageCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LastLearnedLanguageCardRepository extends CrudRepository<LastLearnedLanguageCard, Integer> {
    @Transactional
    void deleteByUserId(int userId);
    List<LastLearnedLanguageCard> findAllByUserId(int userId);
}
