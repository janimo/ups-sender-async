package ro.startx.ups.server;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static ro.startx.ups.server.util.JsonHelpers.jsonFixture;

public class MessageTest {

  @Test
  public void testMinimal() throws IOException {
    Message message = Message.newBuilder()
                             .withToken("1")
                             .withAppID("appid")
                             .withExpireOn("2016-01-01T00:00:00.000Z")
                             .build();

    assertEquals(message.serialize(), jsonFixture("fixtures/message-minimal.json"));
  }

  @Test
  public void testComplete() throws IOException {
    Message message = Message.newBuilder()
                             .withToken("1")
                             .withAppID("appid")
                             .withExpireOn("2016-01-01T00:00:00.000Z")
                             .build();

    assertEquals(message.serialize(), jsonFixture("fixtures/message-complete.json"));
  }

  @Test
  public void testWithData() throws IOException {
    Message message = Message.newBuilder()
                             .withToken("2")
                             .withAppID("appid")
                             .withExpireOn("2016-01-01T00:00:00.000Z")
                             .withDataPart("key1", "value1")
                             .withDataPart("key2", "value2")
                             .build();

    assertEquals(message.serialize(), jsonFixture("fixtures/message-data.json"));
  }

}
