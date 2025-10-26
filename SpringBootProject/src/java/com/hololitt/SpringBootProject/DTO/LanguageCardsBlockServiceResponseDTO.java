package com.hololitt.SpringBootProject.DTO;

import com.hololitt.SpringBootProject.enums.OperationStatus;
import com.hololitt.SpringBootProject.models.LanguageCardsBlock;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class LanguageCardsBlockServiceResponseDTO {
    public LanguageCardsBlockServiceResponseDTO(boolean success, OperationStatus operationStatus){
        this.success = success;
        this.operationStatus = operationStatus;
    }
    private boolean success;
    private OperationStatus operationStatus;
    private LanguageCardsBlock languageCardsBlock;
}
