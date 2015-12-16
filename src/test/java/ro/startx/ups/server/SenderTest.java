package ro.startx.ups.server;

import com.google.common.util.concurrent.ListenableFuture;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import com.squareup.okhttp.mockwebserver.rule.MockWebServerRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static ro.startx.ups.server.util.FixtureHelpers.fixture;
import static ro.startx.ups.server.util.JsonHelpers.jsonFixture;

public class SenderTest {

  @Rule
  public MockWebServerRule server = new MockWebServerRule();

  @Test
  public void testSuccess() throws InterruptedException, ExecutionException, TimeoutException, IOException {
    MockResponse successResponse = new MockResponse().setResponseCode(200)
                                                     .setBody(fixture("fixtures/response-success.json"));
    server.enqueue(successResponse);

    String                   context = "my context";
    Sender                   sender  = new Sender(10, server.getUrl("/ups/send").toExternalForm());
    ListenableFuture<Result> future  = sender.send(Message.newBuilder().withAppID("appid").withToken("1").withExpireOn("2016-01-01T00:00:00.000Z").build(), context);

    Result result = future.get(10, TimeUnit.SECONDS);

    assertEquals(result.isSuccess(), true);
    assertEquals(result.isUnregistered(), false);
    assertNull(result.getError());
    assertNull(result.getMessage());
    assertEquals(context, result.getContext());

    RecordedRequest request = server.takeRequest();
    assertEquals(request.getPath(), "/ups/send");
    assertEquals(new String(request.getBody()), jsonFixture("fixtures/message-minimal.json"));
    assertEquals(request.getHeader("Content-Type"), "application/json");
    assertEquals(server.getRequestCount(), 1);
  }

  @Test
  public void testBadRequest() throws TimeoutException, InterruptedException {
    MockResponse malformed = new MockResponse().setResponseCode(400);
    server.enqueue(malformed);

    Sender                   sender = new Sender(10, server.getUrl("/ups/send").toExternalForm());
    ListenableFuture<Result> future = sender.send(Message.newBuilder().withAppID("appid").withToken("1").build());

    try {
      future.get(10, TimeUnit.SECONDS);
      throw new AssertionError();
    } catch (ExecutionException e) {
      assertTrue(e.getCause() instanceof InvalidRequestException);
    }

    assertEquals(server.getRequestCount(), 1);
  }

  @Test
  public void testServerError() throws TimeoutException, InterruptedException {
    MockResponse error = new MockResponse().setResponseCode(503);
    server.enqueue(error);
    server.enqueue(error);
    server.enqueue(error);

    Sender sender = new Sender(2, server.getUrl("/ups/send").toExternalForm());
    ListenableFuture<Result> future = sender.send(Message.newBuilder().withAppID("appid").withToken("1").build());

    try {
      future.get(10, TimeUnit.SECONDS);
      throw new AssertionError();
    } catch (ExecutionException ee) {
      assertTrue(ee.getCause() instanceof ServerFailedException);
    }

    assertEquals(server.getRequestCount(), 3);
  }

  @Test
  public void testServerErrorRecovery() throws InterruptedException, ExecutionException, TimeoutException {
    MockResponse success = new MockResponse().setResponseCode(200)
                                             .setBody(fixture("fixtures/response-success.json"));

    MockResponse error = new MockResponse().setResponseCode(503);

    server.enqueue(error);
    server.enqueue(error);
    server.enqueue(error);
    server.enqueue(success);

    Sender sender = new Sender(3, server.getUrl("/ups/send").toExternalForm());
    ListenableFuture<Result> future = sender.send(Message.newBuilder().withAppID("appid").withToken("1").build());

    Result result = future.get(10, TimeUnit.SECONDS);

    assertEquals(server.getRequestCount(), 4);
    assertEquals(result.isSuccess(), true);
    assertEquals(result.isUnregistered(), false);
    assertNull(result.getMessage());
    assertNull(result.getError());
  }

  @Test
  public void testNetworkError() throws TimeoutException, InterruptedException, IOException {
    MockResponse response = new MockResponse().setResponseCode(200)
                                              .setBody(fixture("fixtures/response-success.json"));

    server.enqueue(response);
    server.enqueue(response);
    server.enqueue(response);

    Sender sender = new Sender(2, server.getUrl("/ups/send").toExternalForm());

    server.get().shutdown();

    ListenableFuture<Result> future = sender.send(Message.newBuilder().withAppID("appid").withToken("1").build());

    try {
      future.get(10, TimeUnit.SECONDS);
    } catch (ExecutionException e) {
      assertTrue(e.getCause() instanceof IOException);
    }
  }

  @Test
  public void testNotRegistered() throws InterruptedException, ExecutionException, TimeoutException {
    MockResponse response = new MockResponse().setResponseCode(200)
                                              .setBody(fixture("fixtures/response-not-registered.json"));

    server.enqueue(response);

    Sender                   sender = new Sender(2, server.getUrl("/ups/send").toExternalForm());
    ListenableFuture<Result> future = sender.send(Message.newBuilder()
                                                         .withAppID("appid").withToken("2")
                                                         .withDataPart("message", "new message!")
                                                         .build());

    Result result = future.get(10, TimeUnit.SECONDS);

    assertFalse(result.isSuccess());
    assertTrue(result.isUnregistered());
    assertEquals(result.getError(), "unknown-token");
  }
}
