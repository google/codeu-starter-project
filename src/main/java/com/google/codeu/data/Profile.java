package com.google.codeu.data;

public class Profile {

  private String email = "N/A";
  private String name = "N/A";
  private String profilePicURL;
  private Double latitude = 37.422;
  private Double longitude = -122.084;
  private String phone = "N/A";
  private String schedule = "N/A";

  public Profile() {
  }

  public Profile(String email, String profilePicURL, String name, Double latitude, 
      Double longitude, String phone, String schedule) {
    this.email = email;
    this.name = name;
    this.profilePicURL = profilePicURL;
    this.latitude = latitude;
    this.longitude = longitude;
    this.phone = phone;
    this.schedule = schedule;
  }

  public String getEmail() {
    return email;
  }
  
  public String getName() {
    return name;
  }
  
  public String getProfilePicURL() {
    return profilePicURL;
  }
  
  public String getPhone() {
    return phone;
  }
  
  public String getSchedule() {
    return schedule;
  }
  
  public Double getLatitude() {
    return latitude;
  }
  
  public Double getLongitude() {
    return longitude;
  }
}