package com.google.codeu.data;

/** A class to represent the data we want to store in user maps. */
public class UserMarker {

  private double lat;
  private double lng;
  private String content;

  /** Set private variables to paramters. */
  public UserMarker(double lat, double lng, String content) {
    this.lat = lat;
    this.lng = lng;
    this.content = content;
  }

  public double getLat() {
    return lat;
  }

  public double getLng() {
    return lng;
  }

  public String getContent() {
    return content;
  }
}
