package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.repositorys.LanguageCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LanguageCardService {
    private final LanguageCardRepository languageCardRepository;
  private final UserService userService;
@Autowired
    public LanguageCardService(LanguageCardRepository languageCardRepository, UserService userService){
        this.languageCardRepository = languageCardRepository;
        this.userService = userService;
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
  public void deleteLanguageCardById(int id, long userId){
        languageCardRepository.deleteByIdAndUserId(id, userId);
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
public boolean isLanguageCardExists(String word, String translation){
        return languageCardRepository.existsByWordAndTranslation(word, translation);
}
    public LanguageCard searchLanguageCard(String searchingType, String value){
        LanguageCard foundedLanguageCard = null;
        switch(searchingType){
            case "find by word" ->
                    foundedLanguageCard = findLanguageCardByWordAndUserId(value,
                            (int) userService.getUserId());
            case "find by translation" ->
                    foundedLanguageCard = findLanguageCardByTranslationAndUserId(value,
                            (int) userService.getUserId());
        }
        return foundedLanguageCard;
    }
}
