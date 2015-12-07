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
package org.whispersystems.ups.server.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpsResponseEntity {

  @JsonProperty
  private boolean ok;

  @JsonProperty
  private String message;

  @JsonProperty
  private String error;

  public boolean getOK() {
    return ok;
  }

  public String getMessage() {
    return message;
  }

  public String getError() {
    return error;
  }
}
