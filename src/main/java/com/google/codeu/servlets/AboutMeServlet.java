package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Responds with a hard-coded message for testing purposes.
 */
@WebServlet("/about")
public class AboutMeServlet extends HttpServlet{
  
 @Override
 public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws IOException {
  
  response.getOutputStream().println("hello world");
 }
}