package com.acompany.httpmonitor.utilities;

import com.acompany.httpmonitor.pojo.LogRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogStreamerTest
{
    private static final String VALID_DATA_FILE = "data/valid-test-data.csv";
    private static final String INVALID_DATA_FILE = "data/invalid-test-data.csv";
    private static final int NUM_DATA_LINES = 3;
    private static final int NUM_DATA_LINES_INVALID = 2;
    private static final String TEST_IP = "10.0.0.";
    private static final String TEST_RFC = "-";
    private static final String TEST_AUTHUSER = "apache";
    private static final Instant TEST_TIMESTAMP = Instant.ofEpochSecond(1549573860L);
    private static final HttpRequestType[] TEST_HTTP_REQUEST_TYPE =
            new HttpRequestType[] { HttpRequestType.GET, HttpRequestType.POST, HttpRequestType.PUT };
    private static final String[] TEST_SECTION = new String[] { "/api", "/report", "/api" };
    private static final String TEST_PROTOCOL = "HTTP/1.0";
    private static final Integer[] TEST_STATUS = new Integer[] { 200, 201, 202 };
    private static final Long TEST_BYTES = 1200L;

    private LogStreamer logStreamer;
    private LogStreamer invalidLogStreamer;

    @BeforeEach
    void setUp() throws IOException
    {
        ClassLoader classLoader = LogStreamerTest.class.getClassLoader();

        String dataSource = requireNonNull(classLoader.getResource(VALID_DATA_FILE)).getPath();
        this.logStreamer = new LogStreamer(dataSource);

        dataSource = requireNonNull(classLoader.getResource(INVALID_DATA_FILE)).getPath();
        this.invalidLogStreamer = new LogStreamer(dataSource);
    }

    @Test
    void hasNext()
    {
        for (int i = 0; i < NUM_DATA_LINES; ++i) {
            assertTrue(
                    logStreamer.hasNext(),
                    "hasNext() mismatch");
        }
    }

    @Test
    void hasNextInvalid()
    {
        for (int i = 0; i < NUM_DATA_LINES_INVALID; ++i) {
            assertTrue(
                    invalidLogStreamer.hasNext(),
                    "hasNext() mismatch");
        }
    }

    @Test
    void next()
    {
        for (int i = 0; i < NUM_DATA_LINES; ++i) {
            LogRecord logRecord = logStreamer.next();

            assertTrue(
                    logRecord.isValid(),
                    "isValid mismatch");

            assertEquals(
                    TEST_IP + i,
                    logRecord.getRemoteHost(),
                    "remoteHost mismatch");

            assertEquals(
                    TEST_RFC,
                    logRecord.getRfc931(),
                    "rfc931 mismatch");

            assertEquals(
                    TEST_AUTHUSER,
                    logRecord.getAuthUser(),
                    "authUser mismatch");

            assertEquals(
                    TEST_TIMESTAMP.plusSeconds(i),
                    logRecord.getTimestamp(),
                    "timestamp mismatch");

            assertEquals(
                    TEST_HTTP_REQUEST_TYPE[i],
                    logRecord.getHttpRequestType(),
                    "httpRequest mismatch");

            assertEquals(
                    TEST_SECTION[i],
                    logRecord.getSection(),
                    "section mismatch");

            assertEquals(
                    TEST_PROTOCOL,
                    logRecord.getProtocol(),
                    "protocol mismatch");

            assertEquals(
                    TEST_STATUS[i],
                    logRecord.getStatus(),
                    "status mismatch");

            assertEquals(
                    TEST_BYTES + i,
                    logRecord.getBytes(),
                    "bytes mismatch");
        }
    }

    @Test
    void nextInvalid()
    {
        for (int i = 0; i < NUM_DATA_LINES_INVALID; ++i) {
            LogRecord logRecord = invalidLogStreamer.next();

            assertEquals(
                    i != 0,
                    logRecord.isValid(),
                    "isValid mismatch");

            assertEquals(
                    i == 0 ? "" : TEST_IP + i,
                    logRecord.getRemoteHost(),
                    "remoteHost mismatch");

            assertEquals(
                    i == 0 ? "" : TEST_RFC,
                    logRecord.getRfc931(),
                    "rfc931 mismatch");

            assertEquals(
                    i == 0 ? "" : TEST_AUTHUSER,
                    logRecord.getAuthUser(),
                    "authUser mismatch");

            assertEquals(
                    i == 0 ? Instant.EPOCH : TEST_TIMESTAMP.plusSeconds(i),
                    logRecord.getTimestamp(),
                    "timestamp mismatch");

            assertEquals(
                    i == 0 ? HttpRequestType.GET : TEST_HTTP_REQUEST_TYPE[i],
                    logRecord.getHttpRequestType(),
                    "httpRequest mismatch");

            assertEquals(
                    i == 0 ? "" : TEST_SECTION[i],
                    logRecord.getSection(),
                    "section mismatch");

            assertEquals(
                    i == 0 ? "" : TEST_PROTOCOL,
                    logRecord.getProtocol(),
                    "protocol mismatch");

            assertEquals(
                    i == 0 ? 0 : TEST_STATUS[i],
                    logRecord.getStatus(),
                    "status mismatch");

            assertEquals(
                    i == 0 ? 0L : TEST_BYTES + i,
                    logRecord.getBytes(),
                    "bytes mismatch");
        }
    }
}
