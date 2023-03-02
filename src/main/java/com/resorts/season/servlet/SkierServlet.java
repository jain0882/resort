package com.resorts.season.servlet;

import com.google.gson.Gson;
import com.resorts.season.PerformanceStats;
import com.resorts.season.model.LiftRide;
import com.resorts.season.model.Resort;
import com.resorts.season.model.ResortSkier;
import com.resorts.season.model.Season;
import com.resorts.season.repository.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {
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
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if(pathInfo.matches("/(.*)/seasons/(.*)/days/(.*)/skiers/(.*)"))
        {
            String[] pathParts = pathInfo.split("/");
            Integer resortId = Integer.parseInt(pathParts[1]);
            Optional<Resort> optionalResort = resortRepository.findById(resortId.longValue());
            if(!optionalResort.isPresent())
            {
                response.sendError(404, "Resort not found");
            }
            else
            {
                Season season = seasonRepository.findBySeasonAndResortId(resortId.longValue(), pathParts[3]);
                Long resortSkierCount = resortSkierRepository.findByDayOnResort(pathParts[5], resortId.longValue());
                response.getWriter().println(resortSkierCount);

                HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
                map.putIfAbsent("getSkierDayVertical", map.getOrDefault("getSkierDayVertical", 0l));
            }
        }
        else if(pathInfo.matches("/(.*)/vertical"))
        {
            String[] pathParts = pathInfo.split("/");
            Integer totalVertical = skierVerticalRepository.findTotalVerticalBySeasonId(pathParts[1]);
            response.getWriter().println(totalVertical);

            HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
            map.putIfAbsent("getSkierResortTotals", map.getOrDefault("getSkierResortTotals", 0l));
        }
        else
        {
            response.sendError(400, "wrong url path");
        }
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        try
        {
            String[] pathInfoParts = pathInfo.split("/");
            if(pathInfoParts.length != 8
                    || !pathInfoParts[2].equalsIgnoreCase("seasons")
                    || !pathInfoParts[4].equalsIgnoreCase("days")
                    || !pathInfoParts[6].equalsIgnoreCase("skiers")
                )
            {
                response.sendError(400, "Invalid input");
            }
            else
            {
                Integer resortId = Integer.parseInt(pathInfoParts[1]);
                Optional<Resort> optionalResort = resortRepository.findById(resortId.longValue());
                if(!optionalResort.isPresent())
                {
                    response.sendError(404, "Data not found");
                }
                else
                {
                    String seasonId = pathInfoParts[3];
                    Season season = seasonRepository.findBySeason(seasonId);
                    if(season == null)
                    {
                        response.sendError(404, "Data not found");
                    }
                    else
                    {
                        Integer dayId = Integer.parseInt(pathInfoParts[5]);
                        if(dayId < 1 || dayId > 365)
                        {
                            response.sendError(400, "Invalid input");
                        }
                        else
                        {
                            Integer skierId = Integer.parseInt(pathInfoParts[7]);
                            LiftRide liftRide = new LiftRide();
                            liftRide.setTime(dayId);
                            liftRideRepository.save(liftRide);

                            response.getWriter().println("201 Write Successful");

                            HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
                            map.putIfAbsent("writeNewLiftRide", map.getOrDefault("writeNewLiftRide", 0l));
                        }
                    }
                }
            }
        } catch (Exception ex)
        {
            response.sendError(400, "Invalid input " + ex.getMessage());
        }
    }
}

