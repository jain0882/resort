package com.resorts.season.servlet;

import com.google.gson.Gson;
import com.resorts.season.PerformanceStats;
import com.resorts.season.model.Resort;
import com.resorts.season.model.ResortSkier;
import com.resorts.season.model.Season;
import com.resorts.season.repository.ResortRepository;
import com.resorts.season.repository.ResortSkierRepository;
import com.resorts.season.repository.SeasonRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ResortServlet")
public class ResortServlet extends HttpServlet {
    @Autowired
    private ResortRepository resortRepository;
    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private ResortSkierRepository resortSkierRepository;
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if(pathInfo.equals("/")) //resorts
        {
            List<Resort> listResort = resortRepository.findAll();
            String listResortString = new Gson().toJson(listResort);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(listResortString);
            out.flush();

            HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
            map.putIfAbsent("getResorts", map.getOrDefault("getResorts", 0l));
        }
        else if(pathInfo.matches("/(.*)/seasons/(.*)/day/(.*)/skiers"))
        {
            try
            {
                String[] pathParts = pathInfo.split("/");
                Integer resortId = Integer.parseInt(pathParts[1]);
                Optional<Resort> optionalResort = resortRepository.findById(resortId.longValue());
                if(!optionalResort.isPresent())
                {
                    response.sendError(400, "Invalid resortId supplied");
                }
                else
                {
                    Long countResortSkier = resortSkierRepository.findByDayOnResort(pathParts[5], resortId.longValue());
                    response.getWriter().println(countResortSkier);
                }

                HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
                map.putIfAbsent("getResortSkiersDay", map.getOrDefault("getResortSkiersDay", 0l));
            }
            catch (Exception ex)
            {
                response.sendError(400, "Invalid input");
            }
        }
        else if(pathInfo.matches("/(.*)/seasons"))
        {
            try
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
                    List<Season> seasonList = seasonRepository.findByResortId(resortId.longValue());

                    String listSeasons = new Gson().toJson(seasonList);

                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    out.print(listSeasons);
                    out.flush();

                    HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
                    map.putIfAbsent("getResortSeasons", map.getOrDefault("getResortSeasons", 0l));
                }
            }
            catch (Exception ex)
            {
                response.sendError(400, "Invalid input");
            }
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
        //path validation
        String[] pathParts = pathInfo.split("/");
        try
        {
            if(pathParts.length != 3
                    || !pathParts[2].equalsIgnoreCase("seasons")) {
            response.sendError(400, "Invalid input");
            }
            else {
                Integer resortId = Integer.parseInt(pathParts[1]);
                Optional<Resort> resort = resortRepository.findById(resortId.longValue());
                if(!resort.isPresent())
                {
                    response.sendError(404, "Resort not found");
                }
                else
                {
                    String body = request.getReader().lines().reduce("", String::concat);
                    JSONObject obj = new JSONObject(body);

                    Season season = new Season();
                    season.setSeason(obj.getString("season"));
                    seasonRepository.save(season);

                    response.getWriter().println("201 new season created");

                    HashMap<String, Long> map = PerformanceStats.getPerformanceStats();
                    map.putIfAbsent("addSeason", map.getOrDefault("addSeason", 0l));
                }
            }
        }
        catch (Exception ex)
        {
            response.sendError(400, "Invalid input: " + ex.getMessage());
        }
    }
}
