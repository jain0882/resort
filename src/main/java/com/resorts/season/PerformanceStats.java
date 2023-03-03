package com.resorts.season;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class PerformanceStats {
    private static HashMap<String, Long> performanceStats;
    private PerformanceStats()
    {
    }

    public static HashMap<String, Long> getPerformanceStats()
    {
        if(performanceStats == null)
        {
            performanceStats =  new HashMap<>();
        }

        return performanceStats;
    }
}
