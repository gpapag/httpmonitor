package com.acompany.httpmonitor.utilities;

import lombok.NonNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configurator
{
    private static final long DEFAULT_STATS_INTERVAL_SEC = 10L;
    private static final long DEFAULT_ALERT_INTERVAL_SEC = 2 * 60L;
    private static final double DEFAULT_ALERT_THRESHOLD = 10.0D;
    private static final String DEFAULT_DATA_FILE = "sample.csv";

    private boolean isValidConfig;
    private Long statsIntervalSec = null;
    private Long alertIntervalSec = null;
    private Double alertThreshold = null;
    private String dataFile = null;

    public Configurator()
    {
        this.isValidConfig = true;
    }

    public Configurator(@NonNull String propertiesFile)
    {
        this.isValidConfig = true;
        try {
            isValidConfig = parseConfigurationFile(propertiesFile);
        }
        catch (IOException e) {
            isValidConfig = false;
        }
    }

    private boolean parseConfigurationFile(String propertiesFile) throws IOException
    {
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesFile));

        properties.forEach((propertyKey, propertyValue) -> {
            String propertyKeyStr = propertyKey.toString();
            String propertyValueStr = propertyValue.toString();

            System.out.println(String.format("Property[%s] Value[%s]", propertyKeyStr, propertyValueStr));

            switch (propertyKeyStr) {
                case "stats_interval_sec":
                    statsIntervalSec = Long.valueOf(propertyValueStr);
                    break;
                case "alert_interval_sec":
                    alertIntervalSec = Long.valueOf(propertyValueStr);
                    break;
                case "alert_threshold_rps":
                    alertThreshold = Double.valueOf(propertyValueStr);
                    break;
                case "data_file":
                    dataFile = propertyValueStr;
                    break;
                default:
                    System.err.println(String.format("Ignoring unknown Key[%s] with Value[%s]", propertyKeyStr, propertyValueStr));
                    break;
            }
        });

        return (statsIntervalSec == null || statsIntervalSec > 0)
                       && (alertIntervalSec == null || alertIntervalSec > 0)
                       && (alertThreshold == null || alertThreshold > 0)
                       && (dataFile == null || !dataFile.isEmpty());
    }

    public boolean isValid()
    {
        return isValidConfig;
    }

    public long getStatsIntervalSec()
    {
        return statsIntervalSec == null
                       ? DEFAULT_STATS_INTERVAL_SEC
                       : statsIntervalSec;
    }

    public long getAlertIntervalSec()
    {
        return alertIntervalSec == null
                       ? DEFAULT_ALERT_INTERVAL_SEC
                       : alertIntervalSec;
    }

    public double getAlertThreshold()
    {
        return alertThreshold == null
                       ? DEFAULT_ALERT_THRESHOLD
                       : alertThreshold;
    }

    public String getDataFile()
    {
        return dataFile == null
                       ? DEFAULT_DATA_FILE
                       : dataFile;
    }
}
