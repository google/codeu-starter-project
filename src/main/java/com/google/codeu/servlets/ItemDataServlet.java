package com.google.codeu.servlets;

import com.google.codeu.data.Item;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Handles fetching postings
 */
@WebServlet("/item-data")
public class ItemDataServlet extends HttpServlet {
  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String user = request.getParameter("user");
    Item postingData = datastore.getPosting(user);
    if (postingData == null) {
      postingData = new Item();
    }
    Gson gson = new Gson();
    String json = gson.toJson(postingData);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // redirect user if they not logged in
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    // get parameters about post
    String userEmail = userService.getCurrentUser().getEmail();
    String title = Jsoup.clean(request.getParameter("title"), Whitelist.none());
    String price = request.getParameter("price");
    String description = Jsoup.clean(request.getParameter("description"), Whitelist.none());

    // create an item and store in Datastore
    Item item = new Item(title, Double.parseDouble(price), userEmail, description);
    datastore.storePosting(item);
    response.sendRedirect("/user-page.html?user=" + userEmail);
  }
}
