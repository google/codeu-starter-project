package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Handles fetching user profile data for matches feed
 * @author Alan
 *
 */
@WebServlet("/matches")
public class MatchesFeedServlet extends HttpServlet{
  
  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }
  
  /**
   * Responds with json of all users about me sections
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }
    
    List<User> users = datastore.getAllUsers();
    Gson gson = new Gson();
    String json = gson.toJson(users);

    response.getOutputStream().println(json);
  }
}
