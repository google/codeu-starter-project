package com.google.codeu.data;

public class Item {

  private String title;
  private Double price;
  private String email;
  private String description;
  
  /* Item class. Stores information about a specific item posted by a user*/
  // TODO: add image support

  public Item(String title, Double price, String email, String description) {
    this.title = title;
    this.price = price;
    this.email = email;
    this.description = description;
  }

  public String getEmail() {
    return email;
  }
  
  public String getTitle() {
    return title;
  }

  public Double getPrice() {
    return price;
  }
  
  public String getDescription() {
    return description;
  }

}