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
package ro.startx.ups.server.internal;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpsRequestEntity {

  @JsonProperty
  private String appid;

  @JsonProperty
  private String token;

  @JsonProperty(value = "expire_on")
  private String expireOn;

  @JsonProperty(value = "replace_tag")
  private String replaceTag;

  @JsonProperty(value = "clear_pending")
  private Boolean clearPending;

  @JsonProperty(value = "data")
  private Map<String, String> data;


  public UpsRequestEntity(String appid, String token, String expireOn, String replaceTag, Boolean clearPending,
                          Map<String, String> data)
  {
    this.appid     = appid;
    this.token             = token;
    this.expireOn  = expireOn;
    this.replaceTag  = replaceTag;
    this.clearPending  = clearPending;
    this.data            = data;
  }
}
