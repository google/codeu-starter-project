package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles fetching site statistics.
 */
@WebServlet("/stats")
public class StatsPageServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with site statistics in JSON.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    response.setContentType("application/json");

    int messageCount = datastore.getTotalMessageCount();
    int longestMessage = datastore.getLongestMessageCount();
    int totalUsers = datastore.getTotalUserCount();
    ArrayList<String> topPosters = datastore.getTopUsers();

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("messageCount", messageCount);
    jsonObject.addProperty("longestMessageLength", longestMessage);
    jsonObject.addProperty("userCount", totalUsers);
    jsonObject.addProperty("topUser1", topPosters.get(0));
    jsonObject.addProperty("topUser2", topPosters.get(1));
    jsonObject.addProperty("topUser3", topPosters.get(2));
    response.getOutputStream().println(jsonObject.toString());
  }
}