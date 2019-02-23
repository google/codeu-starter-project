package com.google.codeu.servlets;

import java.io.IOException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Handles fetching and saving user data.
 */
@WebServlet("/about")
public class AboutMeServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }
 
 /**
  * Responds with the "about me" section for a particular user.
  */
   @Override
 public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("text/html");
    String user = request.getParameter("user");
    if(user == null || user.equals("")) {
      return;
    }
  
    String aboutMe = "This is " + user + "'s about me.";
  
    response.getOutputStream().println(aboutMe);
  }
 
  @Override
 public void doPost(HttpServletRequest request, HttpServletResponse response)
     throws IOException {
    UserService userService = UserServiceFactory.getUserService();  
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }
  
    String userEmail = userService.getCurrentUser().getEmail();

    System.out.println("Saving about me for " + userEmail);
  // TODO: save the data
  
    response.sendRedirect("/user-page.html?user=" + userEmail);
  }
}