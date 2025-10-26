package com.hololitt.SpringBootProject.DTO;

import com.hololitt.SpringBootProject.enums.OperationStatusRepository;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatsDTO {
private final int languageCardsAmount;
private final OperationStatusRepository operationStatus;
}
