package com.github.gpapag.httpmonitor;

import com.github.gpapag.httpmonitor.pojo.LogRecord;
import com.github.gpapag.httpmonitor.pojo.TrafficStats;
import com.github.gpapag.httpmonitor.utilities.Configurator;
import com.github.gpapag.httpmonitor.utilities.LogStreamer;
import lombok.NonNull;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class StatsExtractor
{
    private static final int STATS_TOP_SECTIONS_HITS = 3;

    private final long statsIntervalSec;
    private final LogStreamer logStreamer;
    private final TrafficStatsCollector trafficStatsCollector;
    private final AlertManager alertManager;

    public StatsExtractor(@NonNull Configurator configurator) throws IOException
    {
        this.statsIntervalSec = configurator.getStatsIntervalSec();

        this.logStreamer = new LogStreamer(configurator.getDataFile());
        this.trafficStatsCollector = new TrafficStatsCollector();
        this.alertManager = new AlertManager(configurator.getAlertIntervalSec(), configurator.getAlertThreshold());
    }

    public boolean processLogStream()
    {
        long numInvalidRecords = 0L;
        Instant startStatsInterval;

        LogRecord firstValidLogRecord = findFirstValidLogRecord();
        if (firstValidLogRecord == null) {
            return false;
        }

        startStatsInterval = firstValidLogRecord.getTimestamp();

        trafficStatsCollector.update(firstValidLogRecord);
        alertManager.registerEvent(firstValidLogRecord);

        while (logStreamer.hasNext()) {
            LogRecord logRecord = logStreamer.next();

            if (!logRecord.isValid()) {
                ++numInvalidRecords;
                break;
            }

            trafficStatsCollector.update(logRecord);
            alertManager.registerEvent(logRecord);

            Instant currentTimeIntstant = logRecord.getTimestamp();
            if (currentTimeIntstant.minusSeconds(statsIntervalSec).isAfter(startStatsInterval)) {
                printSectionsStats(
                        startStatsInterval,
                        currentTimeIntstant,
                        trafficStatsCollector.retrieveTopK(STATS_TOP_SECTIONS_HITS),
                        numInvalidRecords);

                trafficStatsCollector.reset();
                startStatsInterval = currentTimeIntstant;
            }
        }

        return true;
    }

    private LogRecord findFirstValidLogRecord()
    {
        LogRecord logRecord = null;

        while (logStreamer.hasNext()) {
            logRecord = logStreamer.next();

            if (logRecord.isValid()) {
                break;
            }
        }

        return logRecord;
    }

    private void printSectionsStats(Instant intervalStartTime, Instant intervalEndTime, List<TrafficStats> topSections, long numInvalidRecords)
    {
        String intervalBounds = String.format("\nInterval[%d, %d]", intervalStartTime.getEpochSecond(), intervalEndTime.getEpochSecond());
        System.out.println(intervalBounds);

        for (TrafficStats trafficStats : topSections) {
            System.out.println("\t" + trafficStats.toString());
        }
        System.out.println("\tNumber of invalid records: " + numInvalidRecords);
    }
}
