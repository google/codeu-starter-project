package com.google.codeu.data;

public class Profile {

  private String email;
  private String name;
  private String phone;
  private String schedule;

  public Profile() {
  }

  public Profile(String email, String name, String phone, String schedule) {
    this.email = email;
    this.name = name;
    this.phone = phone;
    this.schedule = schedule;
  }

  public String getEmail() {
    return email;
  }
  
  public String getName() {
    return name;
  }
  
  public String getPhone() {
    return phone;
  }
  
  public String getSchedule() {
    return schedule;
  }
}