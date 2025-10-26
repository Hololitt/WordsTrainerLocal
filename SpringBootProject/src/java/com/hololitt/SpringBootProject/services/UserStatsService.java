package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.DTO.UsersStatsDTO;
import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.User;
import com.hololitt.SpringBootProject.models.UserStats;
import com.hololitt.SpringBootProject.repositorys.LanguageCardRepository;
import com.hololitt.SpringBootProject.repositorys.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserStatsService {
    private final UserRepository userRepository;
private final LanguageCardRepository languageCardRepository;

    public Integer calculateLanguageCardsCount(int userId){
        return languageCardRepository.calculateLanguageCards(userId);
    }
  public Integer calculateTotalMistakesCount(int userId){
        return languageCardRepository.calculateTotalMistakesCount(userId);
  }
  public UsersStatsDTO getUsersStats(){
        List<User> userList = userRepository.findAll();
        Set<UserStats> userStats = new HashSet<>();

      for(User user: userList){
          Set<LanguageCard> languageCards = user.getLanguageCards();
          int learnedLanguageCardsAmount = languageCards.size();
          int totalMistakesCount = 0;
          int languageCardsRepetitionCount = 0;
          String username = user.getName();

          for(LanguageCard lc : languageCards){
totalMistakesCount += lc.getMistakesCount();
languageCardsRepetitionCount += lc.getRepeatCount();
          }
          UserStats us = UserStats.builder()
                  .username(username)
                  .totalMistakesCount(totalMistakesCount)
                  .languageCardsRepetitionCount(languageCardsRepetitionCount)
                  .countLanguageCards(learnedLanguageCardsAmount)
                  .build();
          userStats.add(us);
      }

        return new UsersStatsDTO(userStats);
  }
}
