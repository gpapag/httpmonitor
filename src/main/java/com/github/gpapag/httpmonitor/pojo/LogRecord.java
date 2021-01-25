package com.github.gpapag.httpmonitor.pojo;

import com.github.gpapag.httpmonitor.utilities.HttpRequestType;
import java.time.Instant;
import lombok.Getter;
import lombok.NonNull;

public class LogRecord {
  @Getter private final boolean isValid;
  @Getter private final String remoteHost;
  @Getter private final String rfc931;
  @Getter private final String authUser;
  @Getter private final Instant timestamp;
  @Getter private final HttpRequestType httpRequestType;
  @Getter private final String section;
  @Getter private final String resource;
  @Getter private final String protocol;
  @Getter private final Integer status;
  @Getter private final Long bytes;

  public LogRecord(
      @NonNull boolean isValid,
      @NonNull String remoteHost,
      @NonNull String rfc931,
      @NonNull String authUser,
      @NonNull Instant timestamp,
      @NonNull HttpRequestType httpRequestType,
      @NonNull String section,
      @NonNull String resource,
      @NonNull String protocol,
      @NonNull Integer status,
      @NonNull Long bytes) {
    this.isValid = isValid;
    this.remoteHost = remoteHost;
    this.rfc931 = rfc931;
    this.authUser = authUser;
    this.timestamp = timestamp;
    this.httpRequestType = httpRequestType;
    this.section = section;
    this.resource = resource;
    this.protocol = protocol;
    this.status = status;
    this.bytes = bytes;
  }
}
