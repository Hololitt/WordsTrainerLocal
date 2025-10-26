package com.hololitt.SpringBootProject.services;

import com.hololitt.SpringBootProject.DTO.LanguageCardNoteDTO;
import com.hololitt.SpringBootProject.enums.OperationStatus;
import com.hololitt.SpringBootProject.models.LanguageCardNote;
import com.hololitt.SpringBootProject.repositorys.LanguageCardNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LanguageCardNoteService {

    private final LanguageCardNoteRepository languageCardNoteRepository;

    public LanguageCardNoteDTO save(Set<LanguageCardNote> languageCardNoteSet){
        if(languageCardNoteSet == null || languageCardNoteSet.isEmpty()){
            return LanguageCardNoteDTO.builder()
                    .operationStatus(OperationStatus.INVALID_INPUT)
                    .build();
        }
        languageCardNoteRepository.saveAll(languageCardNoteSet);
        return LanguageCardNoteDTO.builder()
                .operationStatus(OperationStatus.OK)
                .build();
    }


    public LanguageCardNoteDTO findAllByUserId(long userId){
        Set<LanguageCardNote> languageCardNoteSet = languageCardNoteRepository.findAllByUserId(userId);

        if(languageCardNoteSet == null || languageCardNoteSet.isEmpty()){
            return LanguageCardNoteDTO.builder()
                    .operationStatus(OperationStatus.NOT_FOUND)
                    .build();
        }

        return LanguageCardNoteDTO.builder()
                .operationStatus(OperationStatus.OK)
                .languageCardNoteSet(languageCardNoteSet)
                .build();
    }
}
