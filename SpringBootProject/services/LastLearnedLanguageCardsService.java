package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.repositorys.LanguageCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LastLearnedLanguageCardsService {

private final LanguageCardRepository languageCardRepository;


    @CacheEvict(value = "lastLearnedLanguageCards", key = "#userId")
    public void deleteCache(int userId){}
@Cacheable(value = "lastLearnedLanguageCards", key = "#userId")
public List<LanguageCard> findLastLearnedLanguageCards(int userId){
    return languageCardRepository.findLastLearnedCards(userId);
}

@Transactional
public void deleteStatusLastLearnedCard(int userId){
        List<LanguageCard> languageCards = findLastLearnedLanguageCards(userId);

        languageCards.forEach(lc -> lc.setIsLastLearned(false));

        languageCardRepository.saveAll(languageCards);
}
}