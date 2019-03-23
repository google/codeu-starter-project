package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
/**
 * Handles fetching and saving user data.
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with the "profile" section for a particular user.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    response.setContentType("text/html");

    String user = request.getParameter("user");

    if (user == null || user.equals("")) {
      // Request is invalid, return empty response
      return;
    }

    User userData = datastore.getUser(user);

    if (userData == null || userData.getProfile() == null) {
      return;
    }

    //TO-DO change to for loop for printing
    for (String info : userData.getProfile())
    {
      response.getOutputStream().println(info);
    }
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
    
    String name = Jsoup.clean(request.getParameter("name"), Whitelist.none());
    String phone = Jsoup.clean(request.getParameter("phone"), Whitelist.none());
    String schedule = Jsoup.clean(request.getParameter("schedule"), Whitelist.none());
    
    String[] userProfile = {name, phone, schedule};
    
    User user = new User(userEmail, userProfile);
    datastore.storeUser(user);

    response.sendRedirect("/user-page.html?user=" + userEmail);
  }
}