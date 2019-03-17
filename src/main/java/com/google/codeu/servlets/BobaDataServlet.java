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
  private int nameIdx = 2;
  private int ratingIdx = 3;
  private int addressIdx = 4;
  private int cityIdx = 5;
  private int latIdx = 6;
  private int longIdx = 7;
  private String bobaCSV = "/WEB-INF/bayarea_boba_spots.csv";

  JsonArray bobaShopsArray;

  @Override
  public void init() {
    this.bobaShopsArray = parseCSV(this.bobaCSV);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getOutputStream().println(bobaShopsArray.toString());
  }

  /**
   * Read and parse data from the specified csv file into JsonArray.
   */
  private JsonArray parseCSV(String path) {
    bobaShopsArray = new JsonArray();
    Gson gson = new Gson();

    Scanner scanner = getCSV(path);
    scanner.nextLine(); //Skip header line

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      String name = cells[nameIdx];
      String address = cells[addressIdx] + ", " + cells[cityIdx];
      double rating = Double.parseDouble(cells[ratingIdx]);
      double lat = Double.parseDouble(cells[latIdx]);
      double lng = Double.parseDouble(cells[longIdx]);

      bobaShopsArray.add(gson.toJsonTree(new BobaShop(name, address, rating, lat, lng)));
    }

    scanner.close();
    return bobaShopsArray;
  }

  /**
   * Returns a scanner on the specified csv file.
   */
  private Scanner getCSV(String path){
    return new Scanner(getServletContext().getResourceAsStream(path));
  }

  /**
   * BobaShop class, stores info about a boba shop.
   */
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