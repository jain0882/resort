package com.resorts.season.repository;

import com.resorts.season.model.SkierVertical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SkierVerticalRepository extends JpaRepository<SkierVertical, Long> {
    @Query(value = "select sv.totalVert from skier_vertical sv where sv.seasonId = :season", nativeQuery = true)
    public Integer findTotalVerticalBySeasonId(String season);
}
