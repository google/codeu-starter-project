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

package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Handles fetching and saving {@link Message} instances. */
@WebServlet("/messages")
public class MessageServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with a JSON representation of {@link Message} data for a specific user. Responds with
   * an empty array if the user is not provided.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");

    String user = request.getParameter("user");

    if (user == null || user.equals("")) {
      // Request is invalid, return empty array
      response.getWriter().println("[]");
      return;
    }

    List<Message> messages = datastore.getMessages(user);
    Gson gson = new Gson();
    String json = gson.toJson(messages);

    response.getWriter().println(json);
  }

  /** Stores a new {@link Message}. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String user = userService.getCurrentUser().getEmail();
    String text = Jsoup.clean(request.getParameter("text"), Whitelist.none());
    String recipient = request.getParameter("recipient");
    float sentimentScore = getSentimentScore(text);
    String messageCategories = "";
    if (getNumWords(text) > 20) {
      messageCategories = getMessageCategories(text).keySet().toString(); 
    } else {
      messageCategories = "";
    }

    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");
    
    Message message = new Message(user, text, recipient, sentimentScore, messageCategories);
    
    if (blobKeys != null && !blobKeys.isEmpty()) {
      BlobKey blobKey = blobKeys.get(0);
      ImagesService imagesService = ImagesServiceFactory.getImagesService();
      ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
      String imageUrl = imagesService.getServingUrl(options);
      message.setImageUrl(imageUrl);
    }
    
    datastore.storeMessage(message);

    response.sendRedirect("/user-page.html?user=" + recipient);
  }
  
  /** Calculates the sentiment score of a message. */
  private float getSentimentScore(String text) throws IOException {
    Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();

    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    languageService.close();

    return sentiment.getScore();
  }
  
  /** Determines the appropriate categories of a message > 20 words. */
  private HashMap<String, Float> getMessageCategories(String text) throws IOException {
    HashMap<String, Float> messageCategories = new HashMap<String, Float>();
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      // set content to the text string
      Document doc = Document.newBuilder()
          .setContent(text)
          .setType(Type.PLAIN_TEXT)
          .build();
      ClassifyTextRequest request = ClassifyTextRequest.newBuilder()
          .setDocument(doc)
          .build();
      // detect categories in the given text
      ClassifyTextResponse response = language.classifyText(request);

      for (ClassificationCategory category : response.getCategoriesList()) {
        messageCategories.put(category.getName(), category.getConfidence());
      }
    }
    return messageCategories;
  }
  
  /** Determines the number of words in a given string. */ 
  public int getNumWords(String text) {
    if (text == null) {
      return 0;
    }
    String trimmedText = text.trim();
    String[] textWords = trimmedText.split("\\s+");
    
    return textWords.length;
  }
}
