package com.resorts.season.servlet;

import com.resorts.season.PerformanceStats;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Iterator;

@WebServlet(name = "StaticsServlet")
public class StatisticsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(PerformanceStats.getPerformanceStats().size() == 0)
        {
            response.getWriter().println("empty list");
        }
        else
        {
            Iterator<String> it = PerformanceStats.getPerformanceStats().keySet().iterator();
            while(it.hasNext())
            {
                String operation = it.next();
                response.getWriter().println(operation + "-" + PerformanceStats.getPerformanceStats().get(operation));
            }
        }
    }
}
