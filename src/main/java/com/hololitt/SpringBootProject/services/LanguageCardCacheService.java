package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.LanguageCard;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LanguageCardCacheService {
    private final LanguageCardService languageCardService;
    private final UserService userService;
    public LanguageCardCacheService(LanguageCardService languageCardService, UserService userService){
        this.languageCardService = languageCardService;
        this.userService = userService;
    }
    private final Map<Long, List<LanguageCard>> cachedLanguageCardsByUser = new HashMap<>();

    public List<LanguageCard> getLanguageCardsByUser() {
        long userId = userService.getUserId();
        if (cachedLanguageCardsByUser.containsKey(userId)) {
            return cachedLanguageCardsByUser.get(userId);
        } else {
            List<LanguageCard> languageCards = languageCardService.getLanguageCardsByUserId(userId);
            cachedLanguageCardsByUser.put(userId, languageCards);
            return languageCards;
        }
    }
    public void updateLanguageCardsForUser(long userId) {
        List<LanguageCard> languageCards = languageCardService.getLanguageCardsByUserId(userId);
        cachedLanguageCardsByUser.put(userId, languageCards);
    }
}
