package com.github.gpapag.httpmonitor.utilities;

import com.github.gpapag.httpmonitor.pojo.LogRecord;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Iterator;
import java.util.NoSuchElementException;
import lombok.NonNull;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class LogStreamer implements Iterator<LogRecord> {
  private final Iterator<CSVRecord> csvRecordIterator;

  public LogStreamer(@NonNull String dataSource) throws IOException {
    File csvFile = new File(dataSource);
    InputStream csvInputStream = new FileInputStream(csvFile);
    InputStreamReader input = new InputStreamReader(csvInputStream);
    CSVParser csvParser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(input);

    this.csvRecordIterator = csvParser.iterator();
  }

  @Override
  public boolean hasNext() {
    return csvRecordIterator.hasNext();
  }

  @Override
  public LogRecord next() {
    if (!hasNext()) {
      throw new NoSuchElementException("no LogRecord available");
    }

    boolean isValid;
    String remoteHost;
    String rfc931;
    String authUser;
    Instant timestamp;
    HttpRequestType httpRequestType;
    String section;
    String resource;
    String protocol;
    Integer status;
    Long bytes;
    try {
      CSVRecord csvRecord = csvRecordIterator.next();

      isValid = true;
      remoteHost = csvRecord.get(0);
      rfc931 = csvRecord.get(1);
      authUser = csvRecord.get(2);
      timestamp = Instant.ofEpochSecond(Long.parseLong(csvRecord.get(3)));
      httpRequestType = retrieveHttpRequestType(csvRecord.get(4));
      section = retrieveSection(csvRecord.get(4));
      resource = retrieveResource(csvRecord.get(4));
      protocol = retrieveProtocol(csvRecord.get(4));
      status = Integer.valueOf(csvRecord.get(5));
      bytes = Long.valueOf(csvRecord.get(6));
    } catch (NumberFormatException e) {
      isValid = false;
      remoteHost = "";
      rfc931 = "";
      authUser = "";
      timestamp = Instant.EPOCH;
      httpRequestType = HttpRequestType.GET;
      section = "";
      resource = "";
      protocol = "";
      status = 0;
      bytes = 0L;
    }

    return new LogRecord(
        isValid,
        remoteHost,
        rfc931,
        authUser,
        timestamp,
        httpRequestType,
        section,
        resource,
        protocol,
        status,
        bytes);
  }

  private HttpRequestType retrieveHttpRequestType(String request) {
    String s = request.split("\\s")[0];

    HttpRequestType httpRequestType;

    switch (s.toLowerCase()) {
      case "get":
        httpRequestType = HttpRequestType.GET;
        break;
      case "head":
        httpRequestType = HttpRequestType.HEAD;
        break;
      case "post":
        httpRequestType = HttpRequestType.POST;
        break;
      case "put":
        httpRequestType = HttpRequestType.PUT;
        break;
      case "delete":
        httpRequestType = HttpRequestType.DELETE;
        break;
      case "connect":
        httpRequestType = HttpRequestType.CONNECT;
        break;
      case "options":
        httpRequestType = HttpRequestType.OPTIONS;
        break;
      case "trace":
        httpRequestType = HttpRequestType.TRACE;
        break;
      case "patch":
        httpRequestType = HttpRequestType.PATCH;
        break;
      default:
        httpRequestType = null;
    }

    return httpRequestType;
  }

  private String retrieveSection(String request) {
    String s = request.split("\\s")[1];
    int idx = s.indexOf('/', 1);

    if (idx < 0) {
      return s;
    }

    return s.substring(0, idx);
  }

  private String retrieveResource(String request) {
    String s = request.split("\\s")[1];
    int idx = s.indexOf('/', 1);

    if (idx < 0) {
      return "";
    }

    return s.substring(idx);
  }

  private String retrieveProtocol(String request) {
    return request.split("\\s")[2];
  }
}
