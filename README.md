# HTTP Stats
Parses information from HTTP log records and produces statistics and alerts based on configurable parameters.

## Usage
To produce the statistics/alerts execute:
```text
httpmonitor <monitor.properties>
```
where `monitor.properties` contains the values of the configuration parameters:
* `stats_interval_sec`: duration of interval (in **seconds**) over which traffic statistics are computed (default value 10sec).
* `alert_interval_sec`: duration of interval (in **seconds**) over which alert conditions are computed (default value 120sec).
* `alert_threshold_rps`: alert threshold (in **requests per second**) (default value 10.0rps).
* `data_file`: csv file with HTTP log records (default value sample.csv).

All the output is printed on the console. Error messages are printed on the stderr.


## Compilation
The code is developed using Java 15 and built using Maven 3.6.3.

To build the code run:
```text
mvn clean package
```
This creates `target/httpmonitor` which an executable wrapper script that executes the code in `target/httpmonitor-<version>.jar`.

