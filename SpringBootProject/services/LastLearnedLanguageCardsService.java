package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.LastLearnedLanguageCard;
import com.hololitt.SpringBootProject.repositorys.LastLearnedLanguageCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LastLearnedLanguageCardsService {

private final LastLearnedLanguageCardRepository lastLearnedLanguageCardRepository;

    @CachePut(value = "lastLearnedLanguageCards", key = "#userId")
    @Transactional
    public void updateLastLearnedLanguageCards(List<LastLearnedLanguageCard> languageCards, int userId){
        lastLearnedLanguageCardRepository.deleteAllByUserId(userId);
        lastLearnedLanguageCardRepository.saveAll(languageCards);
    }
    @CacheEvict(value = "lastLearnedLanguageCards", key = "#userId")
    public void deleteCache(int userId){}
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
        languageCard.setLastRepetition(lc.getLastRepetition());
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
            languageCard.setLastRepetition(lastCard.getLastRepetition());

            return languageCard;
        }).collect(Collectors.toList());
    }
}