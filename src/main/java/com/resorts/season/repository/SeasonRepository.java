package com.resorts.season.repository;

import com.resorts.season.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    @Query(value = "select * from season s where s.season = :season", nativeQuery = true)
    public Season findBySeason(String season);

    @Query(value = "select * from season s where s.resort_id = :resortId", nativeQuery = true)
    public List<Season> findByResortId(Long resortId);

    @Query(value = "select * from season s where s.resort_id = :resortId and s.season = :season", nativeQuery = true)
    public Season findBySeasonAndResortId(Long resortId, String season);
}
