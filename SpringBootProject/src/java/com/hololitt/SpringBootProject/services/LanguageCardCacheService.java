package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.LanguageCardCreationForm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LanguageCardCacheService {
    private final LanguageCardService languageCardService;
    private final UserService userService;
    private final Map<Long, List<LanguageCard>> createdLanguageCardsByUsers = new ConcurrentHashMap<>();
    private LanguageCard languageCardToEdit;
    private final Map<Long, List<LanguageCard>> cachedLanguageCardsByUser = new ConcurrentHashMap<>();
    public void setLanguageCardToEdit(LanguageCard languageCardToEdit){
        this.languageCardToEdit = languageCardToEdit;
    }
    public LanguageCard getLanguageCardToEdit(){
        return languageCardToEdit;
    }
    public LanguageCardCacheService(LanguageCardService languageCardService, UserService userService){
        this.languageCardService = languageCardService;
        this.userService = userService;
    }
    public void deleteCreatedLanguageCards(long userId){
        if(createdLanguageCardsByUsers.get(userId) != null){
            createdLanguageCardsByUsers.clear();
        }
    }
    public void addCreatedLanguageCard(LanguageCardCreationForm languageCardCreationForm, long userId) {
        String word = languageCardCreationForm.getWord();
        String translation = languageCardCreationForm.getTranslation();

        LanguageCard newCard = new LanguageCard(word, translation, userId);

        List<LanguageCard> userCards = createdLanguageCardsByUsers.computeIfAbsent(userId, k -> new ArrayList<>());

        userCards.add(newCard);
    }
    public List<LanguageCard> getCreatedLanguageCards(long userId){
        return createdLanguageCardsByUsers.get(userId);
    }
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

    public void updateCreatedLanguageCard(LanguageCardCreationForm oldCard, LanguageCardCreationForm newCard, long userId){
        List<LanguageCard> languageCards = createdLanguageCardsByUsers.get(userId);

        String oldWord = oldCard.getWord();
        String oldTranslation = oldCard.getTranslation();

        for (LanguageCard card : languageCards) {
            if (card.getWord().equals(oldWord) && card.getTranslation().equals(oldTranslation)) {
                card.setWord(newCard.getWord());
                card.setTranslation(newCard.getTranslation());
                break;
            }
        }
    }
}
