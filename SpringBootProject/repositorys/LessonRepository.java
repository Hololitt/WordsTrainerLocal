package com.hololitt.SpringBootProject.repositorys;

import com.hololitt.SpringBootProject.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    Lesson findByTitle(String title);
}
