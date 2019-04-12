package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Profile;

import jdk.internal.jline.internal.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    System.out.println("--------------------------PROFILE SERVLET GET--------------------------");
    response.setContentType("application/json");

    String user = request.getParameter("user");

    if (user == null || user.equals("")) {
      // Request is invalid, return empty response
      return;
    }

    Profile profileData = datastore.getProfile(user);

    if (profileData == null) {
      profileData = new Profile();
    }
    
    Gson gson = new Gson();
    String json = gson.toJson(profileData);

    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    System.out.println("--------------------------PROFILE SERVLET POST--------------------------");
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String userEmail = userService.getCurrentUser().getEmail();
    
    String name = Jsoup.clean(request.getParameter("name"), Whitelist.none());
    Double latitude = Double.valueOf(Jsoup.clean(request.getParameter("latitude"), Whitelist.none()));
    Double longitude = Double.valueOf(Jsoup.clean(request.getParameter("longitude"), Whitelist.none()));
    String phone = Jsoup.clean(request.getParameter("phone"), Whitelist.none());
    String schedule = Jsoup.clean(request.getParameter("schedule"), Whitelist.none());

    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("profile_pic");

    String profilePicURL = "";
    
    if(blobKeys != null && !blobKeys.isEmpty()) {
      BlobKey blobKey = blobKeys.get(0);
      ImagesService imagesService = ImagesServiceFactory.getImagesService();
      ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
      profilePicURL = imagesService.getServingUrl(options);
    }
    
    Profile profile = new Profile(userEmail, profilePicURL, name, latitude, longitude, phone, schedule);
    datastore.storeProfile(profile);

    response.sendRedirect("/user-page.html?user=" + userEmail);
  }
}