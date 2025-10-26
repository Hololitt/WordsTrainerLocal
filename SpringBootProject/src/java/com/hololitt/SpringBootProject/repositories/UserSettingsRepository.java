package com.hololitt.SpringBootProject.repositories;

import com.hololitt.SpringBootProject.models.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    UserSettings findByUserId(long userId);
}
