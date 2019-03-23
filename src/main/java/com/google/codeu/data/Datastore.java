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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore; 
  private static int longestMessage = 0;
  private static HashMap<String, Integer> postsPerUser = new HashMap<String,Integer>();
  private static HashMap<String, Integer> messageCategoryCount = new HashMap<String, Integer>();

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the Message in Datastore. */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    messageEntity.setProperty("user", message.getUser().toString());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());
    messageEntity.setProperty("recipient", message.getRecipient());
    messageEntity.setProperty("sentimentScore", message.getSentimentScore());
    
    //If there are more than 20 words, perform content classification
    if (getNumWords(message.getText()) > 20) {
      messageEntity.setProperty("messageCategories", message.getMessageCategories());
      
      String messageCategories = message.getMessageCategories().trim();
      String[] messageCategoryList = messageCategories.split("/");

      for (String category : messageCategoryList) {
        category = category.trim();
        category = category.replace("[", "");
        category = category.replaceAll("]", "");

        if (!messageCategoryCount.containsKey(category)) {
          messageCategoryCount.put(category, 1);
        } else {
          messageCategoryCount.put(category, messageCategoryCount.get(category) + 1);        
        }
      }
    } else {
      messageEntity.setProperty("messageCategories", "");
    }
    datastore.put(messageEntity);

    int messageLength = message.getText().length();
    
    if (messageLength > longestMessage) {
      longestMessage = messageLength;
    }
    postsPerUser.put(message.getUser(), getMessages(message.getUser()).size());
    
  }

  /**
   * Gets messages sent to a specific recipient.
   *
   * @return a list of messages sent to the recipient, or empty list if recipient
   *     has never received has never posted a
   *     List is sorted by time descending.
   */
  public List<Message> getMessages(String recipient) {
    Query query =
        new Query("Message")
        .setFilter(new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient))
        .addSort("timestamp", SortDirection.DESCENDING);
    List<Message> messages = fetchMessages(query);

    return messages;
  }

  /**
   * Gets messages posted by all users.
   *
   * @return a list of messages posted by all users, or an empty list if no user has posted a 
   *     message. List is sorted by time descending.
   */
  public List<Message> getAllMessages() {
    Query query =
        new Query("Message")
        .addSort("timestamp", SortDirection.DESCENDING);
    List<Message> messages = fetchMessages(query);

    return messages;
  }

  /**
   * Retrieves list of messages for a specific user.
   *
   * @return a list of results, or empty list if no results found
   */
  public List<Message> fetchMessages(Query query) {
    PreparedQuery results = datastore.prepare(query);
    List<Message> messages = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();

        UUID id = UUID.fromString(idString);    
        String user = (String) entity.getProperty("user");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");
        String recipient = (String) entity.getProperty("recipient"); 
        // sentimentScore casted to Double from float first to avoid it being saved as a 0
        float sentimentScore = entity.getProperty("sentimentScore") == null ? (float) 0.0 : 
            ((Double) entity.getProperty("sentimentScore")).floatValue();
        String messageCategories = (String) entity.getProperty("messageCategories");         

        // Replace all image URLS in message with proper image HTML tags
        String regex = "(https?://([^\\s.]+.?[^\\s.]*)+/[^\\s.]+.(png|jpg))";
        String replacement = "<img src=\"$1\" />";
        String textWithImagesReplaced = text.replaceAll(regex, replacement);

        Message message = new Message(id, user, textWithImagesReplaced, timestamp, 
            recipient, sentimentScore, messageCategories);

        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }
    return messages;
  }



  /** Returns the total number of messages for all users. */
  public int getTotalMessageCount() {
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));

  }



  /* About me Section */
  /** Stores the User in Datastore. */
  public void storeUser(User user) {
    Entity userEntity = new Entity("User", user.getEmail());
    userEntity.setProperty("email", user.getEmail());
    //modified
    userEntity.setProperty("userProfile", user.getProfile());
    datastore.put(userEntity);
  }

  /**
   * Returns the User owned by the email address, or
   * null if no matching User was found.
   */
  public User getUser(String email) {

    Query query = new Query("User")
        .setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    if (userEntity == null) {
      return null;
    }

    //Modified
    String[] userProfile = (String[]) userEntity.getProperty("userProfile");
    User user = new User(email, userProfile);

    return user;
  }

  /** Returns the longest message length of all users. */
  public int getLongestMessageCount() {
    return longestMessage;
  }

  /** Returns the total number of users that have posted. */
  public int getTotalUserCount() {
    return postsPerUser.size();
  }

  /** Returns the top three users that have posted on the website. */
  public ArrayList<String> getTopUsers() {
    ArrayList<String> topUsers = new ArrayList<String>(3);
    int numTopUsers = 3;
    String currTopUser = "";

    //Find the three users with the most posts
    for (int i = numTopUsers; i > 0; i--) {
      int maxPosts = 0;
      currTopUser = "";
      for (String user : postsPerUser.keySet()) {
        if (postsPerUser.get(user) > maxPosts && !topUsers.contains(user)) {
          maxPosts = postsPerUser.get(user);
          currTopUser = user;
        }
      }
      topUsers.add(currTopUser);
    } 
    return topUsers;
  }

  /** Returns the categories and their counts of all of the messages. */
  public String getMessageCategories() {
    String messageCategories = "";
    for (String category : messageCategoryCount.keySet()) {
      messageCategories = messageCategories + "(" + category + " " 
          + messageCategoryCount.get(category) + ")" + " ; ";
    }
    return messageCategories;
  }

  /** Returns the number of words in a given string. */
  public int getNumWords(String text) {
    if (text == null) {
      return 0;
    }
    String trimmedText = text.trim();
    String[] textWords = trimmedText.split("\\s+");
    
    return textWords.length;
  }
}
