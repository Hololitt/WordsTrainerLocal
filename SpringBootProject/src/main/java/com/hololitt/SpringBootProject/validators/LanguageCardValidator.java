package com.hololitt.SpringBootProject.validators;
import com.hololitt.SpringBootProject.models.LanguageCard;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LanguageCardValidator {
    public boolean isLanguageCardListValid(List<LanguageCard> languageCards) {
        return languageCards != null && !languageCards.isEmpty();
    }
}
