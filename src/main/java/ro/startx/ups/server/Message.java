/**
 * Copyright (C) 2015 Open Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ro.startx.ups.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ro.startx.ups.server.internal.UpsRequestEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Message {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private final String              appid;
  private final String              token;
  private final String              expireOn;
  private final String              replaceTag;
  private final Boolean             clearPending;
  private final Map<String, String> data;

  private Message(String appid, String token, String expireOn, String replaceTag, Boolean clearPending,
                          Map<String, String> data)
  {
    this.appid     = appid;
    this.token             = token;
    this.expireOn  = expireOn;
    this.clearPending  = clearPending;
    this.replaceTag  = replaceTag;
    this.data            = data;
  }

  public String serialize() throws JsonProcessingException {
    UpsRequestEntity requestEntity = new UpsRequestEntity(appid, token, expireOn, replaceTag, clearPending, data);

    return objectMapper.writeValueAsString(requestEntity);
  }

  /**
   * Construct a new Message using a Builder.
   * @return A new Builder.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {

    private String              appid     = null;
    private String              token     = null;
    private String              expireOn     = null;
    private String              replaceTag     = null;
    private Boolean             clearPending     = null;
    private Map<String, String> data            = null;

    private Builder() {}

    public Builder withAppID(String appid) {
      this.appid = appid;
      return this;
    }

    public Builder withToken(String token) {
      this.token = token;
      return this;
    }

    public Builder withExpireOn(String expireOn) {
      this.expireOn = expireOn;
      return this;
    }

    public Builder withReplaceTag(String replaceTag) {
      this.replaceTag = replaceTag;
      return this;
    }

    public Builder withDataPart(String key, String value) {
      if (data == null) {
        data = new HashMap<>();
      }
      data.put(key, value);
      return this;
    }


    /**
     * Construct a message object.
     *
     * @return An immutable message object, as configured by this builder.
     */
    public Message build() {
      if (appid.isEmpty() || token.isEmpty()) {
        throw new IllegalArgumentException("You must specify a destination!");
      }

      return new Message(appid, token, expireOn, replaceTag, clearPending, data);
    }
  }


}
