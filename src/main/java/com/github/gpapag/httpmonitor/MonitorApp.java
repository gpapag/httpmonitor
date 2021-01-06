package com.github.gpapag.httpmonitor;

import com.github.gpapag.httpmonitor.utilities.Configurator;

import java.io.IOException;

public class MonitorApp
{
    private static final int STATUS_CODE_USAGE = 1;

    private MonitorApp()
    {
    }

    public static void main(String[] args) throws IOException
    {
        if (args.length > 1) {
            showUsage();
            System.exit(STATUS_CODE_USAGE);
        }

        final Configurator configurator = args.length == 1
                                                  ? new Configurator(args[0])
                                                  : new Configurator();

        final StatsExtractor statsExtractor = new StatsExtractor(configurator);
        final boolean processDone = statsExtractor.processLogStream();
        if (processDone) {
            System.out.println("Done processing traffic logs");
        }
        else {
            System.out.println("Cannot find traffic logs with valid format");
        }
    }

    private static void showUsage()
    {
        System.err.println("Usage: MonitorApp [<app.properties>]\n\t<app.properties>\tconfiguration file");
    }
}
