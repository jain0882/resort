package com.resorts.season.servlet;


import com.resorts.season.PerformanceStats;
import com.resorts.season.model.Resort;
import com.resorts.season.model.Season;
import com.resorts.season.repository.ResortRepository;
import com.resorts.season.repository.ResortSkierRepository;
import com.resorts.season.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/resorts")
public class ResortController {
    @Autowired
    private ResortRepository resortRepository;
    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private ResortSkierRepository resortSkierRepository;
    @GetMapping()
    public List<Resort> getResortList() {
        HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
        map.putIfAbsent("getResorts", map.getOrDefault("getResorts", 0l));

        return resortRepository.findAll();
    }

    @GetMapping("/{resortId}/seasons/{season}/day/{dayId}/skiers")
    public ResponseEntity<Long> getSkierCount(@PathVariable("resortId") Long resortId, @PathVariable("season") String season, @PathVariable("dayId") String dayId)
    {
        Long countResortSkier = 0l;
        try
        {
            Resort resort = resortRepository.findById(resortId).get();
            countResortSkier = resortSkierRepository.findByDayOnResort(dayId, resortId.longValue());

        }
        catch (Exception ex)
        {
            return ResponseEntity.badRequest().build();
        }

        HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
        map.putIfAbsent("getResortSkiersDay", map.getOrDefault("getResortSkiersDay", 0l));
        return  ResponseEntity.ok().body(countResortSkier);
    }

    @GetMapping("/{resortId}/seasons")
    public ResponseEntity<List<Season>> getNumberOfSeasons(@PathVariable("resortId") Long resortId, @PathVariable("season") String season, @PathVariable("dayId") String dayId)
    {
        List<Season> seasonList = null;
        try
        {
            Resort resort = resortRepository.findById(resortId).get();
            seasonList = seasonRepository.findByResortId(resortId.longValue());

        }
        catch (Exception ex)
        {
           return ResponseEntity.badRequest().build();
        }

        HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
        map.putIfAbsent("getResortSeasons", map.getOrDefault("getResortSeasons", 0l));
        return ResponseEntity.ok().body(seasonList);
    }


    @PostMapping("/{resortId}/seasons")
    public ResponseEntity<String> addSeason(@PathVariable("resortId") Long resortId, @RequestBody Season season) {
        try {
            Resort resort = resortRepository.findById(resortId).get();
            seasonRepository.save(season);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
        map.putIfAbsent("addSeason", map.getOrDefault("addSeason", 0l));
        return ResponseEntity.ok().body("season is created");
    }
}
