package org.whispersystems.ups.server;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.whispersystems.ups.server.util.JsonHelpers.jsonFixture;

public class MessageTest {

  @Test
  public void testMinimal() throws IOException {
    Message message = Message.newBuilder()
                             .withToken("1")
                             .withAppID("appname")
                             .build();

    assertEquals(message.serialize(), jsonFixture("fixtures/message-minimal.json"));
  }

  @Test
  public void testComplete() throws IOException {
    Message message = Message.newBuilder()
                             .withToken("1")
                             .withAppID("appname")
                             .build();

    assertEquals(message.serialize(), jsonFixture("fixtures/message-complete.json"));
  }

  @Test
  public void testWithData() throws IOException {
    Message message = Message.newBuilder()
                             .withToken("2")
                             .withAppID("appname")
                             .withDataPart("key1", "value1")
                             .withDataPart("key2", "value2")
                             .build();

    assertEquals(message.serialize(), jsonFixture("fixtures/message-data.json"));
  }

}
