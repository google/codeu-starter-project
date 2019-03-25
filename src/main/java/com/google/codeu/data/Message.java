/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.data;

import java.util.UUID;

/** A single message posted by a user. */
public class Message {
  private String recipient;
  private String imageUrl;
  private UUID id;
  private String user;
  private String text;
  private long timestamp;

  /**
   * Constructs a new {@link Message} posted by {@code user} with {@code text} content. Generates a
   * random ID and uses the current system time for the creation time.
   */
  public Message(String user, String text, String recipient, String imageUrl) {
    this(UUID.randomUUID(), user, text, System.currentTimeMillis(), recipient, imageUrl);
  }


  /**
   * Constructs a new {@link Message} posted by {@code user} with {@code text} content.
   * Sets the private variables to the values taken in from the parameters.
   */
  public Message(UUID id, String user, String text,
    long timestamp, String recipient, String imageUrl) {
    this.id = id;
    this.user = user;
    this.text = text;
    this.timestamp = timestamp;
    this.recipient = recipient;
    this.imageUrl = imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return;
  }

  /* Gets the recipient private member variable*/
  public String getRecipient() {
    return recipient;
  }

  /* Gets the id private member variable */
  public UUID getId() {
    return id;
  }

  /* Gets the user private member variable */
  public String getUser() {
    return user;
  }

  /* Gets the text private member variable */
  public String getText() {
    return text;
  }

  /* Gets the text private member variable */
  public String getImageUrl() {
    return imageUrl;
  }

  /* Set the text private member variable */
  public void setText(String input) {
    this.text = input;
  }

  /* Gets the timestamp private member variable */
  public long getTimestamp() {
    return timestamp;
  }
}
