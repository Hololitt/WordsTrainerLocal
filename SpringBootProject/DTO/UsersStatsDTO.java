package com.hololitt.SpringBootProject.DTO;

import com.hololitt.SpringBootProject.models.UserStats;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UsersStatsDTO {
    public UsersStatsDTO(Set<UserStats> usersStats){
        this.usersStats = usersStats;
        this.localDateTime = LocalDateTime.now();
    }
  private final Set<UserStats> usersStats;

private LocalDateTime localDateTime;
}
