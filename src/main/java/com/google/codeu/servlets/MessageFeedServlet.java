package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles fetching all messages for the public feed.
 */
@WebServlet("/feed")
public class MessageFeedServlet extends HttpServlet{
  
 @Override
 public void doGet(HttpServletRequest request, HttpServletResponse response)
   throws IOException {
  
  response.getOutputStream().println("this will be my message feed");
 }
}