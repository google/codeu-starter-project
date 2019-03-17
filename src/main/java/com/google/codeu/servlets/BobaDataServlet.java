package com.google.codeu.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Returns boba shop data as a JSON array, e.g. [{"lat": 38.4404675, "lng": -122.7144313}].
 */
@WebServlet("/boba-data")
public class BobaDataServlet extends HttpServlet {
  private int name_idx = 2;
  private int rating_idx = 3;
  private int address_idx = 4;
  private int city_idx = 5;
  private int lat_idx = 6;
  private int long_idx = 7;

  JsonArray bobaShopsArray;

  @Override
  public void init() {
    this.bobaShopsArray = parseData();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getOutputStream().println(bobaShopsArray.toString());
  }

  /**
   * Read and parse data from the csv file into JsonArray.
   */
  private JsonArray parseData() {
    bobaShopsArray = new JsonArray();
    Gson gson = new Gson();

    Scanner scanner = 
        new Scanner(getServletContext().getResourceAsStream("/WEB-INF/bayarea_boba_spots.csv"));
  	scanner.nextLine(); //Skip header line

    while(scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      String name = cells[name_idx];
      String address = cells[address_idx] + ", " + cells[city_idx];
      double rating = Double.parseDouble(cells[rating_idx]);
      double lat = Double.parseDouble(cells[lat_idx]);
      double lng = Double.parseDouble(cells[long_idx]);

      bobaShopsArray.add(gson.toJsonTree(new BobaShop(name, address, rating, lat, lng)));
    }

    scanner.close();
    return bobaShopsArray;
  }

  private static class BobaShop {
    String name;
    String address;
    double rating;
    double lat;
    double lng;

    private BobaShop(String name, String address, double rating, double lat, double lng) {
      this.name = name;
      this.address = address;
      this.rating = rating;
      this.lat = lat;
      this.lng = lng;
    }
  }
}