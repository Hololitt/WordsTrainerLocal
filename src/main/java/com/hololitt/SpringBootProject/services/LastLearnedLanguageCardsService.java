package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.LastLearnedLanguageCard;
import com.hololitt.SpringBootProject.repositorys.LastLearnedLanguageCardRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LastLearnedLanguageCardsService {
private final LastLearnedLanguageCardRepository lastLearnedLanguageCardRepository;

public LastLearnedLanguageCardsService(LastLearnedLanguageCardRepository lastLearnedLanguageCardRepository){
    this.lastLearnedLanguageCardRepository = lastLearnedLanguageCardRepository;
}
@CachePut(value = "lastLearnedLanguageCards", key = "#userId")
public void updateLastLearnedLanguageCards(List<LastLearnedLanguageCard> languageCards, int userId){
    deleteLastLearnedLanguageCards(userId);
    lastLearnedLanguageCardRepository.saveAll(languageCards);
}
@CacheEvict(value = "lastLearnedLanguageCards", key = "#userId")
private void deleteLastLearnedLanguageCards(int userId){
    lastLearnedLanguageCardRepository.deleteByUserId(userId);
}
@Cacheable(value = "lastLearnedLanguageCards", key = "#userId")
public List<LastLearnedLanguageCard> findLastLearnedLanguageCards(int userId){
    return lastLearnedLanguageCardRepository.findAllByUserId(userId);
}
public List<LastLearnedLanguageCard> convertToLastLearnedLanguageCards(List<LanguageCard> languageCards){
    return languageCards.stream().map(lc -> {
        LastLearnedLanguageCard languageCard = new LastLearnedLanguageCard();
        languageCard.setWord(lc.getWord());
        languageCard.setTranslation(lc.getTranslation());
        languageCard.setRepeatCount(lc.getRepeatCount());
        languageCard.setId(lc.getId());
        languageCard.setUserId(lc.getUserId());
        languageCard.setMistakesCount(lc.getMistakesCount());
        languageCard.setCreationDate(lc.getCreationDate());

        return languageCard;
    }).collect(Collectors.toList());
}
    public List<LanguageCard> convertToLanguageCards(List<LastLearnedLanguageCard> lastLearnedCards) {
        return lastLearnedCards.stream().map(lastCard -> {
            LanguageCard languageCard = new LanguageCard();

            languageCard.setWord(lastCard.getWord());
            languageCard.setTranslation(lastCard.getTranslation());
            languageCard.setRepeatCount(lastCard.getRepeatCount());
            languageCard.setId(lastCard.getId());
            languageCard.setUserId(lastCard.getUserId());
            languageCard.setMistakesCount(lastCard.getMistakesCount());
            languageCard.setCreationDate(lastCard.getCreationDate());

            return languageCard;
        }).collect(Collectors.toList());
    }
}
