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
  /**
   * Responds with site statistics in JSON.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response){
		Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/ufo_data.csv"));
		// skip title line
		scanner.nextLine();
		while(scanner.hasNextLine()) {
		  String line = scanner.nextLine();
		  String[] cells = line.split(",");
		  // skip incomplete lines
		  if (cells.length < 3) {
		    continue;
		  }
		  String state = cells[0];
		  double lat = Double.parseDouble(cells[1]);
		  double lng = Double.parseDouble(cells[2]);
			 
		  System.out.println("state: " + state);
		  System.out.println("lat: " + lat);
		  System.out.println("lng: " + lng);
		  System.out.println();
		}
		scanner.close();
	}
}