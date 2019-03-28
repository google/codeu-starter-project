package com.google.codeu.servlets;

import com.google.gson.JsonElement;
import com.google.api.client.json.Json;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Handles fetching site statistics.
 */
@WebServlet("/item-data")
public class ItemDataServlet extends HttpServlet {
  JsonElement itemJson;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // TODO: fetch these from DataStore for proper posting ( probably by some ID, title for now)
    String title = "1 Bedroom suite";
    String description = "Really cozy vibe. Includes hydro (sometimes, summer only)";
    String author = "Evil corp.";
    int price = 6000;

    // create an item
    Item item = new Item(title, description, author, price);
    // convert to JSON
    Gson gson = new Gson();
    itemJson = gson.toJsonTree(item);

    response.setContentType("application/json");
    response.getOutputStream().println(itemJson.toString());
  }

  // new private class to store item
  private static class Item {
    String title;
    String description;
    String author;
    int price;

    private Item(String title, String description, String author, int price) {
      this.title = title;
      this.description = description;
      this.author = author;
      this.price = price;
    }
  }


}
