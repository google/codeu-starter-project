package com.google.codeu.servlets;

import java.io.IOException;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

/**
 * Handles fetching site statistics.
 */
@WebServlet("/ufo-data")
public class UfoDataServlet extends HttpServlet {
  JsonArray ufoSightingArray;

  /**
   * Responds with site statistics in JSON.
   */
  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/ufo_data.csv"));
    // storage for ufo_data.csv
    ufoSightingArray = new JsonArray();
    Gson gson = new Gson();
    // skip title line
    scanner.nextLine();
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");
      // skip incomplete lines
      if (cells.length < 3) {
        continue;
      }
      String state = cells[0];
      double lat = Double.parseDouble(cells[1]);
      double lng = Double.parseDouble(cells[2]);
      // add a new UFOSighting object to Json Array
      ufoSightingArray.add(gson.toJsonTree(new UfoSighting(state, lat, lng)));

    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getOutputStream().println(ufoSightingArray.toString());
  }

  // new private class to store UFOU Sightings
  private static class UfoSighting {
    String state;
    double lat;
    double lng;

    private UfoSighting(String state, double lat, double lng) {
      this.state = state;
      this.lat = lat;
      this.lng = lng;
    }
  }

}
