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
 * Returns boba shop data as a JSON array, e.g. [{"lat": 38.4404675, "lng": -122.7144313}]
 */
@WebServlet("/boba-data")
public class BobaDataServlet extends HttpServlet {
	private final int NAME_IDX = 2;
	private final int RATING_IDX = 3;
	private final int ADDRESS_IDX = 4;
	private final int CITY_IDX = 5;
	private final int LAT_IDX = 6;
	private final int LONG_IDX = 7;

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
   * Read and parse data from the csv file into JsonArray
   */
  private JsonArray parseData(){
  	bobaShopsArray = new JsonArray();
    Gson gson = new Gson();

  	Scanner scanner = 
  			new Scanner(getServletContext().getResourceAsStream("/WEB-INF/bayarea_boba_spots.csv"));
  	scanner.nextLine(); //Skip header line

    while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] cells = line.split(",");

			String name = cells[NAME_IDX];
			String address = cells[ADDRESS_IDX] + ", " + cells[CITY_IDX];
			double rating = Double.parseDouble(cells[RATING_IDX]);
			double lat = Double.parseDouble(cells[LAT_IDX]);
			double lng = Double.parseDouble(cells[LONG_IDX]);

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