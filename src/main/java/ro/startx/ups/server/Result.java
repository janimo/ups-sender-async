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

/**
 * The result of an UPS send operation.
 */
public class Result {

  private final Object context;
  private final boolean ok;
  private final String message;
  private final String error;

  Result(Object context, boolean ok, String message, String error) {
    this.context                 = context;
    this.ok = ok;
    this.message               = message;
    this.error                   = error;
  }

  /**
   * @return The assigned UPS message ID, if successful.
   */
  public String getMessage() {
    return message;
  }

  /**
   * @return The raw error string, if present.
   */
  public String getError() {
    return error;
  }

  /**
   * @return If the send was a success.
   */
  public boolean isSuccess() {
    return ok;
  }

  /**
   * @return If the destination UPS registration ID is no longer registered.
   */
  public boolean isUnauthorized() {
    return "unauthorized".equals(error);
  }

  public boolean isUnregistered() {
    return "unknown-token".equals(error);
  }

  /**
   * @return The context passed into Sender.send(), if any.
   */
  public Object getContext() {
    return context;
  }
}
