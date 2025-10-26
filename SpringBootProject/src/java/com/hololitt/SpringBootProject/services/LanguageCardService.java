package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.repositorys.LanguageCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Cacheable(value = "language_cards", key = "#userId")
    public List<LanguageCard> getLanguageCardsByUserId(long userId){
        return languageCardRepository.findByUserId((int) userId);
    }

    @Transactional
    @CachePut(value = "language_cards", key = "#userId")
    public List<LanguageCard> saveLanguageCardList(Set<LanguageCard> languageCardList, long userId){
       return languageCardRepository.saveAll(languageCardList);
    }

    @CacheEvict(value = "language_cards", key = "#userId")
    public void deleteLanguageCardsCache(long userId){}
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
@CachePut(value = "language_cards", key = "#userId")
public LanguageCard updateLanguageCard(LanguageCard newLanguageCard, long userId){
       return languageCardRepository.save(newLanguageCard);
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
       return switch(searchingType){
            case "find by word" ->
                    findLanguageCardByWordAndUserId(value, (int) userService.getUserId());
            case "find by translation" ->
                   findLanguageCardByTranslationAndUserId(value, (int) userService.getUserId());
            default -> throw new IllegalArgumentException();
        };
    }

    public Map<LocalDate, Long> getLearnedWordsStats() {
        return languageCardRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        card -> card.getCreationDate().toLocalDate(),
                        Collectors.counting()
                ));
    }
}
