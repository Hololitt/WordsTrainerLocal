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

public LanguageCard findLanguageCardByWordAndUserId(String word, int userId){
       return languageCardRepository.findByWordAndUserId(word, userId);
}

public LanguageCard findLanguageCardByTranslationAndUserId(String translation, int userId){
        return languageCardRepository.findByTranslationAndUserId(translation, userId);
}
public void updateLanguageCard(LanguageCard newLanguageCard){
        languageCardRepository.save(newLanguageCard);
}

public LanguageCard findLanguageCardByIdAndUserId(int id, long userId){
        return languageCardRepository.findByIdAndUserId(id, userId);
}
public long getCountLanguageCards(long userId){
        return languageCardRepository.countByUserId(userId);
}
}
