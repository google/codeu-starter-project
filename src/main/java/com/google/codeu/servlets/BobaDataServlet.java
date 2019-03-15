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
	//private final int LAT_IDX = 6;
	//private final int LONG_IDX = 7;

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

			// Indices for latitute and longitute
			int long_idx = cells.length - 1;
			int lat_idx = cells.length - 2;

			String name = cells[NAME_IDX];
			double lat = Double.parseDouble(cells[lat_idx]);
			double lng = Double.parseDouble(cells[long_idx]);

			bobaShopsArray.add(gson.toJsonTree(new BobaShop(name, lat, lng)));
    }
    scanner.close();
    return bobaShopsArray;
  }

  private static class BobaShop {
    String name;
    double lat;
    double lng;

    private BobaShop(String name, double lat, double lng) {
      this.name = name;
      this.lat = lat;
      this.lng = lng;
    }
  }
}