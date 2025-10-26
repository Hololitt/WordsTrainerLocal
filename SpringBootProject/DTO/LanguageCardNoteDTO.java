package com.hololitt.SpringBootProject.DTO;

import com.hololitt.SpringBootProject.enums.OperationStatus;
import com.hololitt.SpringBootProject.models.LanguageCardNote;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class LanguageCardNoteDTO {
    private OperationStatus operationStatus;
    private Set<LanguageCardNote> languageCardNoteSet;
}
