package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.repositorys.LanguageCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LanguageCardService {
    private final LanguageCardRepository languageCardRepository;
    public LanguageCardService(LanguageCardRepository languageCardRepository){
        this.languageCardRepository = languageCardRepository;
    }
    public void saveLanguageCard(LanguageCard languageCard){
languageCardRepository.save(languageCard);
    }
    public List<LanguageCard> getLanguageCardList(){
        return languageCardRepository.findAll();
    }
    public List<LanguageCard> getLanguageCardsByUserId(long userId){
        return languageCardRepository.findByUserId((int) userId);
    }
    public void saveLanguageCardList(List<LanguageCard> languageCardList){
        languageCardRepository.saveAll(languageCardList);
    }
    @Transactional
    public void deleteLanguageCardByWordAndUserId(String word, int userId){
languageCardRepository.deleteByUserIdAndWord(userId, word);
    }
public LanguageCard findLanguageCardByWord(String word){
       return languageCardRepository.findByWord(word);
}
public LanguageCard findLanguageCardByTranslation(String translation){
        return languageCardRepository.findByTranslation(translation);
}
public long getCountLanguageCards(long userId){
        return languageCardRepository.countByUserId(userId);
}
}
