package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.DTO.LanguageCardsBlockServiceResponseDTO;
import com.hololitt.SpringBootProject.enums.OperationStatus;
import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.LanguageCardsBlock;
import com.hololitt.SpringBootProject.repositorys.LanguageCardBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class LanguageCardsBlockService {
    private final LanguageCardBlockRepository languageCardBlockRepository;
public LanguageCardsBlockServiceResponseDTO saveLanguageCardBlock(LanguageCardsBlock languageCardsBlock){
        long userId = languageCardsBlock.getUser().getId();
        String typ = languageCardsBlock.getType();

    boolean exists = languageCardBlockRepository.existsByTypeAndUserId(typ, userId);

    if (!exists) {
        languageCardBlockRepository.save(languageCardsBlock);
        return new LanguageCardsBlockServiceResponseDTO(true, OperationStatus.SAVED);
    }

        return new LanguageCardsBlockServiceResponseDTO(false, OperationStatus.ENTITY_EXISTS);
}
    public LanguageCardsBlockServiceResponseDTO existsLanguageCardsBlockByTypAndUserId(String typ, long userId) {
        boolean exists = languageCardBlockRepository.existsByTypeAndUserId(typ, userId);

        return exists
                ? new LanguageCardsBlockServiceResponseDTO(true, OperationStatus.ENTITY_EXISTS)
                : new LanguageCardsBlockServiceResponseDTO(false, OperationStatus.NOT_FOUND);
    }
    public LanguageCardsBlockServiceResponseDTO findLanguageCardsBlockByTypAndUserId(String type, long userId){
        LanguageCardsBlock languageCardsBlock = languageCardBlockRepository.findByTypeAndUserId(type, userId);

        if(languageCardsBlock == null){
            return new LanguageCardsBlockServiceResponseDTO(false, OperationStatus.NOT_FOUND);
        }
        return new LanguageCardsBlockServiceResponseDTO(true, OperationStatus.FOUND, languageCardsBlock);
    }
    public LanguageCardsBlockServiceResponseDTO deleteLanguageCardBlockByTypeAndUserId(String type, long userId) {
        boolean exists = languageCardBlockRepository.existsByTypeAndUserId(type, userId);

        if (exists) {
            languageCardBlockRepository.deleteByTypeAndUserId(type, userId);
            return new LanguageCardsBlockServiceResponseDTO(true, OperationStatus.DELETED);
        }

        return new LanguageCardsBlockServiceResponseDTO(false, OperationStatus.NOT_FOUND);
    }
    public LanguageCardsBlockServiceResponseDTO updateBlockType(String targetBlockType,
                                                                 String currentBlockType, long userId){
        LanguageCardsBlock languageCardsBlock = languageCardBlockRepository.findByTypeAndUserId(currentBlockType, userId);

        if(languageCardsBlock == null){
            return new LanguageCardsBlockServiceResponseDTO(false, OperationStatus.NOT_FOUND);
        }
        boolean newBlockTypeExists = languageCardBlockRepository.existsByTypeAndUserId(targetBlockType, userId);

        if(newBlockTypeExists){
            return new LanguageCardsBlockServiceResponseDTO(false, OperationStatus.ENTITY_EXISTS);
        }

            languageCardsBlock.setType(targetBlockType);
            languageCardBlockRepository.save(languageCardsBlock);

            return new LanguageCardsBlockServiceResponseDTO(true, OperationStatus.UPDATED);
    }

    public LanguageCardsBlockServiceResponseDTO addLanguageCardsList(Set<LanguageCard> languageCards,
                                                                     String blockType, long userId) {
        LanguageCardsBlock languageCardsBlock = validateAndFindLanguageCardsBlock(languageCards, blockType, userId);
        if (languageCardsBlock == null) {
            return new LanguageCardsBlockServiceResponseDTO(false, OperationStatus.INVALID_INPUT);
        }

        languageCardsBlock.getLanguageCards().addAll(languageCards);
        languageCardBlockRepository.save(languageCardsBlock);

        return new LanguageCardsBlockServiceResponseDTO(true, OperationStatus.UPDATED);
    }

    public LanguageCardsBlockServiceResponseDTO addLanguageCard(LanguageCard languageCard,
                                                               String languageCardsBlockType, long userId){
    LanguageCardsBlockServiceResponseDTO blockDTO =
            findLanguageCardsBlockByTypAndUserId(languageCardsBlockType, userId);

    if(!blockDTO.isSuccess()){
        return new LanguageCardsBlockServiceResponseDTO(false, blockDTO.getOperationStatus());
    }
    LanguageCardsBlock languageCardsBlock = blockDTO.getLanguageCardsBlock();

  boolean languageCardExists = languageCardExistsInBlock(languageCard, languageCardsBlock);

  if(!languageCardExists){
      languageCardsBlock.getLanguageCards().add(languageCard);
      return new LanguageCardsBlockServiceResponseDTO(true, OperationStatus.SAVED);
  }
  return new LanguageCardsBlockServiceResponseDTO(false, OperationStatus.ENTITY_EXISTS);
    }

    public LanguageCardsBlockServiceResponseDTO removeLanguageCard(LanguageCard languageCard,
                                                                   String languageCardsBlockType, long userId){
        LanguageCardsBlockServiceResponseDTO blockDTO =
                findLanguageCardsBlockByTypAndUserId(languageCardsBlockType, userId);

        if(!blockDTO.isSuccess()){
            return new LanguageCardsBlockServiceResponseDTO(false, blockDTO.getOperationStatus());
        }

        LanguageCardsBlock languageCardsBlock = blockDTO.getLanguageCardsBlock();

        boolean languageCardExists = languageCardExistsInBlock(languageCard, languageCardsBlock);

        if(!languageCardExists){
            languageCardsBlock.getLanguageCards().remove(languageCard);
            return new LanguageCardsBlockServiceResponseDTO(true, OperationStatus.DELETED);
        }
        return new LanguageCardsBlockServiceResponseDTO(false, OperationStatus.FAILED);
    }

    public LanguageCardsBlockServiceResponseDTO removeLanguageCardsList(Set<LanguageCard> languageCards,
                                                                        String blockType, long userId) {

        LanguageCardsBlock languageCardsBlock = validateAndFindLanguageCardsBlock(languageCards, blockType, userId);
        if (languageCardsBlock == null) {
            return new LanguageCardsBlockServiceResponseDTO(false, OperationStatus.INVALID_INPUT);
        }

        languageCardsBlock.getLanguageCards().removeAll(languageCards);
        languageCardBlockRepository.save(languageCardsBlock);

        return new LanguageCardsBlockServiceResponseDTO(true, OperationStatus.DELETED);
    }

    public boolean languageCardExistsInBlock(LanguageCard languageCard, LanguageCardsBlock languageCardsBlock){
   Set<LanguageCard> languageCards = languageCardsBlock.getLanguageCards();
   return languageCards.contains(languageCard);
    }
    private LanguageCardsBlock validateAndFindLanguageCardsBlock(Set<LanguageCard> languageCards,
                                                                 String blockType, long userId) {
        if (languageCards == null || blockType == null) {
            return null;
        }
        return languageCardBlockRepository.findByTypeAndUserId(blockType, userId);
    }
}
