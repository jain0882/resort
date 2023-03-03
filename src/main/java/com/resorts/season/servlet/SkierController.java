package com.resorts.season.servlet;

import com.resorts.season.PerformanceStats;
import com.resorts.season.model.LiftRide;
import com.resorts.season.model.Resort;
import com.resorts.season.model.Season;
import com.resorts.season.repository.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/skiers")
public class SkierController {
    @Autowired
    private ResortRepository resortRepository;
    @Autowired
    private SeasonRepository seasonRepository;
    @Autowired
    private LiftRideRepository liftRideRepository;
    @Autowired
    private ResortSkierRepository resortSkierRepository;
    @Autowired
    private SkierVerticalRepository skierVerticalRepository;

    @GetMapping("/{resortId}/seasons/{seasonId}/days/{dayID}/skiers/{skierId}")
    public ResponseEntity<Long> getSkierDayVertical(@PathVariable("resortId") Long resortId, @PathVariable("seasonId") String season, @PathVariable("dayID") String dayId, @PathVariable("skierId") Long skierId)
    {
        Long resortSkierCount = 0l;
        try
        {
            Resort resort = resortRepository.findById(resortId).get();
            Season seasonObj = seasonRepository.findBySeasonAndResortId(resortId.longValue(), season);
            resortSkierCount = resortSkierRepository.findByDayOnResort(dayId, resortId.longValue());
        }
        catch (Exception ex)
        {
            return ResponseEntity.badRequest().build();
        }

        HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
        map.putIfAbsent("getSkierDayVertical", map.getOrDefault("getSkierDayVertical", 0l));
        return ResponseEntity.ok().body(resortSkierCount);
    }

    @GetMapping("/{seasonId}/vertical")
    public ResponseEntity<Integer> getSkierVertical(@PathVariable("resortId") String season)
    {
        Integer totalVertical = 0;
        try
        {
            totalVertical = skierVerticalRepository.findTotalVerticalBySeasonId(season);
        }
        catch (Exception ex)
        {
            return ResponseEntity.badRequest().build();
        }

        HashMap<String, Long> map = PerformanceStats.getPerformanceStats();

        map.putIfAbsent("getSkierResortTotals", map.getOrDefault("getSkierResortTotals", 0l));
        return ResponseEntity.ok().body(totalVertical);
    }

    @PostMapping("/{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}")
    public ResponseEntity<String> writeNewLiftRide(@PathVariable("resortID") Long resortId, @PathVariable("seasonID") String seasonId, @PathVariable("dayID") String dayId, @PathVariable("skierID") Long skierId) {
        try {
            Resort resort = resortRepository.findById(resortId).get();
            Season season = seasonRepository.findBySeason(seasonId);
            Integer dayIdInt = Integer.parseInt(dayId);
            if(dayIdInt < 1 || dayIdInt > 365)
            {
                return ResponseEntity.badRequest().build();
            }


            LiftRide liftRide = new LiftRide();
            liftRide.setSeason(seasonId);
            liftRide.setResortId(resortId);
            liftRide.setTime(dayIdInt);
            liftRideRepository.save(liftRide);
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

        HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
        map.putIfAbsent("writeNewLiftRide", map.getOrDefault("writeNewLiftRide", 0l));
        return ResponseEntity.ok().body("LiftRide created");
    }
}

