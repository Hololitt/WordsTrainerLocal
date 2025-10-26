package com.hololitt.SpringBootProject.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageCardFormUpdateRequest {
    private LanguageCardCreationForm oldForm;
    private LanguageCardCreationForm updatedForm;

}
