package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Profile;
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

    response.setContentType("application/json");

    String user = request.getParameter("user");

    if (user == null || user.equals("")) {
      // Request is invalid, return empty response
      return;
    }

    Profile profileData = datastore.getProfile(user);

    if (profileData == null) {
      return;
    }
    
    //TO-DO ask Travis about this gson again
    Gson gson = new Gson();
    String json = gson.toJson(profileData);

    response.getWriter().println(json);
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
    
    
    Profile profile = new Profile(userEmail, name, phone, schedule);
    datastore.storeProfile(profile);

    response.sendRedirect("/user-page.html?user=" + userEmail);
  }
}