package com.github.gpapag.httpmonitor;

import com.github.gpapag.httpmonitor.pojo.LogRecord;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;

public class AlertManager {
  private static final String ALERT_MSG =
      "High traffic generated an alert - hits = %d, triggered at %d";
  private static final String CLEAR_ALERT_MSG = "High traffic alert cleared at %d";

  private final long alertIntervalSec;
  private final double alertThresholdRps;

  private Deque<Instant> eventQueue;
  private boolean alertIsActive;

  public AlertManager(long alertIntevalSec, double alertThresholdRps) {
    this.alertIntervalSec = alertIntevalSec;
    this.alertThresholdRps = alertThresholdRps;

    this.eventQueue = new ArrayDeque<>();
  }

  public void registerEvent(LogRecord logRecord) {
    Instant currentEventInstant = logRecord.getTimestamp();
    eventQueue.addLast(currentEventInstant);

    dropOlderAlertsIfNecessary();

    if (!alertIsActive && shouldActivateAlert()) {
      alertIsActive = true;

      fireAlert();
    }

    if (alertIsActive && shouldDeactivateAlert()) {
      alertIsActive = false;

      deactivateAlert();
    }
  }

  private void dropOlderAlertsIfNecessary() {
    while (!eventQueue.isEmpty()
        && eventQueue.peekFirst().minusSeconds(alertIntervalSec).isAfter(eventQueue.peekLast())) {
      eventQueue.removeFirst();
    }
  }

  private boolean shouldActivateAlert() {
    double numEvents = (double) eventQueue.size();
    double rateRequests = numEvents / alertIntervalSec;

    return Double.compare(rateRequests, alertThresholdRps) > 0;
  }

  private void fireAlert() {
    String alertMessage =
        String.format(
            ALERT_MSG,
            eventQueue.size(),
            eventQueue.size() != 0 ? eventQueue.peekLast().getEpochSecond() : 0);

    System.out.println("\n" + alertMessage + "\n");
  }

  private boolean shouldDeactivateAlert() {
    double numEvents = (double) eventQueue.size();
    double rateRequests = numEvents / alertIntervalSec;

    return Double.compare(rateRequests, alertThresholdRps) <= 0;
  }

  private void deactivateAlert() {
    String clearAlertMessage =
        String.format(
            CLEAR_ALERT_MSG, eventQueue.size() != 0 ? eventQueue.peekLast().getEpochSecond() : 0);

    System.out.println("\n" + clearAlertMessage + "\n");
  }
}
