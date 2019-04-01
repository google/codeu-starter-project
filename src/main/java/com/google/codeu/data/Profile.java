package com.google.codeu.data;

public class Profile {

  private String email;
  private String name;
  //private String profilePicURL;
  private Double latitude;
  private Double longitude;
  private String phone;
  private String schedule;

  public Profile() {
  }

  /**String profilePicURL field redacted. */
  public Profile(String email, String name, Double latitude, 
      Double longitude, String phone, String schedule) {
    this.email = email;
    this.name = name;
    //this.profilePicURL = profilePicURL;
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
  
  /*public String getProfilePicURL() {
    return profilePicURL;
  }*/
  
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