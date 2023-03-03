package com.resorts.season.servlet;

import com.resorts.season.PerformanceStats;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Iterator;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @GetMapping()
    public ResponseEntity<String> getStatistics() {
        if(PerformanceStats.getPerformanceStats().size() == 0)
        {
            return ResponseEntity.ok().body("empty list");
        }
        else
        {
            Iterator<String> it = PerformanceStats.getPerformanceStats().keySet().iterator();
            StringBuilder sb = new StringBuilder();
            while(it.hasNext())
            {
                String operation = it.next();
                sb.append(operation + "-" + PerformanceStats.getPerformanceStats().get(operation));
            }

            return ResponseEntity.ok().body(sb.toString());
        }
    }
}
