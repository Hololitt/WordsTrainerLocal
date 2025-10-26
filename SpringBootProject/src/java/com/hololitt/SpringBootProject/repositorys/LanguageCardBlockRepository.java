package com.hololitt.SpringBootProject.repositorys;

import com.hololitt.SpringBootProject.models.LanguageCardsBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageCardBlockRepository extends JpaRepository<LanguageCardsBlock, Long> {
LanguageCardsBlock findByTypeAndUserId(String typ, long userId);
void deleteByTypeAndUserId(String typ, long userId);
boolean existsByTypeAndUserId(String type, long userId);
}
