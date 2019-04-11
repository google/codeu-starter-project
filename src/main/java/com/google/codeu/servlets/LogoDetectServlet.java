package com.google.codeu.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logo-detect")
public class LogoDetectServlet extends HttpServlet {

  JsonArray logoJsonArray;

  @Override
  public void init() {
    this.logoJsonArray = new JsonArray();
    this.logoJsonArray.add("starbucks");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getOutputStream().println(this.logoJsonArray.toString());
  }
}