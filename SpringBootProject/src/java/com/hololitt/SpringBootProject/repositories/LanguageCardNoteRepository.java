package com.hololitt.SpringBootProject.repositories;

import com.hololitt.SpringBootProject.models.LanguageCardNote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LanguageCardNoteRepository extends CrudRepository<LanguageCardNote, Integer> {
    Set<LanguageCardNote> findAllByUserId(long userId);
}
