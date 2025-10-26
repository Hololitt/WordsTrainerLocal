package com.hololitt.SpringBootProject.controllers;

import com.hololitt.SpringBootProject.DTO.LanguageCardsBlockServiceResponseDTO;
import com.hololitt.SpringBootProject.enums.OperationStatus;
import com.hololitt.SpringBootProject.models.LanguageCard;
import com.hololitt.SpringBootProject.models.LanguageCardsBlock;
import com.hololitt.SpringBootProject.models.User;
import com.hololitt.SpringBootProject.services.LanguageCardsBlockService;
import com.hololitt.SpringBootProject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Home/languageCards/blocks")
@RequiredArgsConstructor
public class LanguageCardsBlockController {
    private final UserService userService;
    private final LanguageCardsBlockService languageCardsBlockService;

@PostMapping("/add")
    public ResponseEntity<HttpStatus> addLanguageCardsToBlock(@RequestBody LanguageCard languageCard,
                                                              @RequestBody String languageCardsBlockType){
    if(languageCard == null && languageCardsBlockType == null){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

LanguageCardsBlockServiceResponseDTO responseDTO = languageCardsBlockService.addLanguageCard(languageCard,
        languageCardsBlockType, userService.getUserId());

return switch(responseDTO.getOperationStatus()){
    case SAVED -> ResponseEntity.status(HttpStatus.OK).build();
    case ENTITY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).build();
    default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
};
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> removeLanguageCardFromBlock(@RequestBody LanguageCard languageCard,
                                                                  @RequestBody String languageCardsBlockType){
        if(languageCard == null && languageCardsBlockType == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        LanguageCardsBlockServiceResponseDTO responseDTO = languageCardsBlockService.removeLanguageCard(languageCard,
                languageCardsBlockType, userService.getUserId());

        if(responseDTO.getOperationStatus().equals(OperationStatus.DELETED)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/update/{currentBlockType}/{targetBlockType}")
    public ResponseEntity<HttpStatus> updateBlockType(@PathVariable String currentBlockType,
                                                      @PathVariable String targetBlockType){
        long userId = userService.getUserId();

        LanguageCardsBlockServiceResponseDTO responseDTO = languageCardsBlockService.updateBlockType(currentBlockType,
                targetBlockType, userId);

       return switch(responseDTO.getOperationStatus()){
            case UPDATED -> buildResponseEntity(HttpStatus.OK);
           case ENTITY_EXISTS -> buildResponseEntity(HttpStatus.CONFLICT);
           case NOT_FOUND -> buildResponseEntity(HttpStatus.NOT_FOUND);
           default -> buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }
    private ResponseEntity<HttpStatus> buildResponseEntity(HttpStatus httpStatus){
        return ResponseEntity.status(httpStatus).build();
    }

    @GetMapping("/find/{typ}")
    public ResponseEntity<LanguageCardsBlock> findLanguageCardsBlockByType(@PathVariable String type){
        long userId = userService.getUserId();

LanguageCardsBlockServiceResponseDTO responseDTO = languageCardsBlockService
        .findLanguageCardsBlockByTypAndUserId(type, userId);

if(responseDTO.isSuccess()){
    return ResponseEntity.ok(responseDTO.getLanguageCardsBlock());
}
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/create/{typ}")
    public ResponseEntity<HttpStatus> createLanguageCardsBlock(@PathVariable String type){
        ResponseEntity<HttpStatus> validationResponse = validateBlockType(type);
        if(validationResponse != null){
            return validationResponse;
        }

        User user = userService.getCurrentUser();
        LanguageCardsBlock languageCardsBlock = new LanguageCardsBlock(type, user);

        LanguageCardsBlockServiceResponseDTO responseDTO = languageCardsBlockService.saveLanguageCardBlock(languageCardsBlock);

   return switch(responseDTO.getOperationStatus()) {
       case SAVED -> ResponseEntity.status(HttpStatus.CREATED).build();
       case ENTITY_EXISTS -> ResponseEntity.status(HttpStatus.CONFLICT).build();
       default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
   };
    }
    @DeleteMapping("/delete/{type}")
    public ResponseEntity<HttpStatus> deleteLanguageCardsBlock(@PathVariable String type){
        ResponseEntity<HttpStatus> validationResponse = validateBlockType(type);
       if(validationResponse != null){
           return validationResponse;
       }

        long userId = userService.getUserId();

        LanguageCardsBlockServiceResponseDTO responseDTO = languageCardsBlockService
                .deleteLanguageCardBlockByTypeAndUserId(type, userId);

        return switch(responseDTO.getOperationStatus()){
            case DELETED -> ResponseEntity.status(HttpStatus.OK).build();
            case NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }
    private ResponseEntity<HttpStatus> validateBlockType(String type){
        if(type == null || type.trim().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return null;
    }
}
