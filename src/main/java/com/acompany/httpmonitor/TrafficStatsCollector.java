package com.acompany.httpmonitor;

import com.acompany.httpmonitor.pojo.LogRecord;
import com.acompany.httpmonitor.pojo.TrafficStats;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TrafficStatsCollector
{
    private final Map<String, TrafficStats> trafficStats;

    public TrafficStatsCollector()
    {
        this.trafficStats = new HashMap<>();
    }

    public void reset()
    {
        trafficStats.clear();
    }

    public void update(@NonNull LogRecord logRecord)
    {
        String trafficStatsKey = logRecord.getSection();

        if (!trafficStats.containsKey(trafficStatsKey)) {
            trafficStats.put(trafficStatsKey, new TrafficStats(trafficStatsKey));
        }

        Long bytes = logRecord.getBytes();
        Integer statusCode = logRecord.getStatus();
        TrafficStats trafficStatsValue = trafficStats.get(trafficStatsKey);

        if (100 <= statusCode && statusCode <= 199) {
            trafficStatsValue.incrementHitsInfo()
                             .incrBytesInfoBy(bytes);
        }
        else if (200 <= statusCode && statusCode <= 299) {
            trafficStatsValue.incrementHitsSucc()
                             .incrBytesSuccBy(bytes);
        }
        else if (300 <= statusCode && statusCode <= 399) {
            trafficStatsValue.incrementHitsRdir()
                             .incrBytesRdirBy(bytes);
        }
        else if (400 <= statusCode && statusCode <= 499) {
            trafficStatsValue.incrementHitsClnt()
                             .incrBytesClntBy(bytes);
        }
        else if (500 <= statusCode && statusCode <= 599) {
            trafficStatsValue.incrementHitsSrvr()
                             .incrBytesSrvrBy(bytes);
        }

        trafficStats.put(trafficStatsKey, trafficStatsValue);
    }

    public List<TrafficStats> retrieveTopK(int k)
    {
        PriorityQueue<TrafficStats> maxHeap = new PriorityQueue<>((o1, o2) -> (-1) * Long.compare(o1.getHits(), o2.getHits()));

        for (Map.Entry<String, TrafficStats> entry : trafficStats.entrySet()) {
            maxHeap.add(entry.getValue());
        }

        List<TrafficStats> topStats = new ArrayList<>();
        for (int i = 0; i < k && !maxHeap.isEmpty(); ++i) {
            topStats.add(maxHeap.poll());
        }

        return topStats;
    }
}
