package com.resorts.season.repository;

import com.resorts.season.model.ResortSkier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResortSkierRepository extends JpaRepository<ResortSkier, Long> {
    @Query(value = "select count(rs) from resort_skier rs where rs.time = :time and rs.resort_id = :resortId", nativeQuery = true)
    public Long findByDayOnResort(String time, Long resortId);
}
