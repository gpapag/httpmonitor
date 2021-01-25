package com.github.gpapag.httpmonitor.pojo;

import lombok.Getter;
import lombok.NonNull;

public class TrafficStats {
  @Getter private final String section;

  @Getter private long hits;
  @Getter private long hitsInfo;
  @Getter private long hitsSucc;
  @Getter private long hitsRdir;
  @Getter private long hitsClnt;
  @Getter private long hitsSrvr;

  @Getter private long bytes;
  @Getter private long bytesInfo;
  @Getter private long bytesSucc;
  @Getter private long bytesRdir;
  @Getter private long bytesClnt;
  @Getter private long bytesSrvr;

  public TrafficStats(@NonNull String section) {
    this.section = section;

    this.hits = 0L;
    this.hitsInfo = 0L;
    this.hitsSucc = 0L;
    this.hitsRdir = 0L;
    this.hitsClnt = 0L;
    this.hitsSrvr = 0L;

    this.bytes = 0L;
    this.bytesInfo = 0L;
    this.bytesSucc = 0L;
    this.bytesRdir = 0L;
    this.bytesClnt = 0L;
    this.bytesSrvr = 0L;
  }

  public TrafficStats incrementHitsInfo() {
    ++hits;
    ++hitsInfo;

    return this;
  }

  public TrafficStats incrementHitsSucc() {
    ++hits;
    ++hitsSucc;

    return this;
  }

  public TrafficStats incrementHitsRdir() {
    ++hits;
    ++hitsRdir;

    return this;
  }

  public TrafficStats incrementHitsClnt() {
    ++hits;
    ++hitsClnt;

    return this;
  }

  public TrafficStats incrementHitsSrvr() {
    ++hits;
    ++hitsSrvr;

    return this;
  }

  public TrafficStats incrBytesInfoBy(long bytes) {
    this.bytes += bytes;
    bytesInfo += bytes;

    return this;
  }

  public TrafficStats incrBytesSuccBy(long bytes) {
    this.bytes += bytes;
    bytesSucc += bytes;

    return this;
  }

  public TrafficStats incrBytesRdirBy(long bytes) {
    this.bytes += bytes;
    bytesRdir += bytes;

    return this;
  }

  public TrafficStats incrBytesClntBy(long bytes) {
    this.bytes += bytes;
    bytesClnt += bytes;

    return this;
  }

  public TrafficStats incrBytesSrvrBy(long bytes) {
    this.bytes += bytes;
    bytesSrvr += bytes;

    return this;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("Section[");
    sb.append(section);
    sb.append("] ");

    sb.append("Hits-Total[");
    sb.append(hits);
    sb.append("] ");

    sb.append("Hits-1xx[");
    sb.append(hitsInfo);
    sb.append("] ");

    sb.append("Hits-2xx[");
    sb.append(hitsSucc);
    sb.append("] ");

    sb.append("Hits-3xx[");
    sb.append(hitsRdir);
    sb.append("] ");

    sb.append("Hits-4xx[");
    sb.append(hitsClnt);
    sb.append("] ");

    sb.append("Hits-5xx[");
    sb.append(hitsSrvr);
    sb.append("] ");

    sb.append("Bytes-Total[");
    sb.append(bytes);
    sb.append("] ");

    sb.append("Bytes-1xx[");
    sb.append(bytesInfo);
    sb.append("] ");

    sb.append("Bytes-2xx[");
    sb.append(bytesSucc);
    sb.append("] ");

    sb.append("Bytes-3xx[");
    sb.append(bytesRdir);
    sb.append("] ");

    sb.append("Bytes-4xx[");
    sb.append(bytesClnt);
    sb.append("] ");

    sb.append("Bytes-5xx[");
    sb.append(bytesSrvr);
    sb.append("]");

    return sb.toString();
  }
}
