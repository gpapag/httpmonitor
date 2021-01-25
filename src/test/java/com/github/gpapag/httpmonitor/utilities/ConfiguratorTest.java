package com.github.gpapag.httpmonitor.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ConfiguratorTest {
  private static final String VALID_PROPERTIES_FILE = "config/valid.properties";
  private static final String INVALID_PROPERTIES_FILE = "config/invalid.properties";
  private static final long EXPECTED_STATS_INTERVAL_SEC = 11L;
  private static final long EXPECTED_ALERT_INTERVAL_SEC = 180L;
  private static final double EXPECTED_ALERT_THRESHOLD_RPS = 11.1D;
  private static final String EXPECTED_DATA_FILE = "sample_csv.txt";
  private static final long DEFAULT_STATS_INTERVAL_SEC = 10L;
  private static final long DEFAULT_ALERT_INTERVAL_SEC = 2 * 60L;
  private static final double DEFAULT_ALERT_THRESHOLD = 10.0D;
  private static final String DEFAULT_DATA_FILE = "sample.csv";

  private static Configurator validConfigurator;
  private static Configurator invalidConfigurator;
  private static Configurator defaultConfigurator;

  @BeforeAll
  static void setUp() {
    ClassLoader classLoader = ConfiguratorTest.class.getClassLoader();

    String propertiesFilename = classLoader.getResource(VALID_PROPERTIES_FILE).getPath();
    validConfigurator = new Configurator(propertiesFilename);

    propertiesFilename = classLoader.getResource(INVALID_PROPERTIES_FILE).getPath();
    invalidConfigurator = new Configurator(propertiesFilename);

    defaultConfigurator = new Configurator();
  }

  @Test
  void isValid() {
    assertTrue(validConfigurator.isValid());
    assertFalse(invalidConfigurator.isValid());
    assertTrue(defaultConfigurator.isValid());
  }

  @Test
  void getStatsIntervalSec() {
    assertEquals(
        EXPECTED_STATS_INTERVAL_SEC,
        validConfigurator.getStatsIntervalSec(),
        "stats_interval_sec mismatch");

    assertEquals(
        DEFAULT_STATS_INTERVAL_SEC,
        defaultConfigurator.getStatsIntervalSec(),
        "default stats_interval_sec mismatch");
  }

  @Test
  void getAlertIntervalSec() {
    assertEquals(
        EXPECTED_ALERT_INTERVAL_SEC,
        validConfigurator.getAlertIntervalSec(),
        "alert_interval_sec mismatch");

    assertEquals(
        DEFAULT_ALERT_INTERVAL_SEC,
        defaultConfigurator.getAlertIntervalSec(),
        "default alert_interval_sec mismatch");
  }

  @Test
  void getAlertThreshold() {
    assertEquals(
        EXPECTED_ALERT_THRESHOLD_RPS,
        validConfigurator.getAlertThreshold(),
        "alert_threshold_rps mismatch");

    assertEquals(
        DEFAULT_ALERT_THRESHOLD,
        defaultConfigurator.getAlertThreshold(),
        "default alert_threshold_rps mismatch");
  }

  @Test
  void getDataFile() {
    assertEquals(EXPECTED_DATA_FILE, validConfigurator.getDataFile(), "data_file mismatch");

    assertEquals(
        DEFAULT_DATA_FILE, defaultConfigurator.getDataFile(), "default data_file mismatch");
  }
}
