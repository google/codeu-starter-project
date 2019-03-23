package com.google.codeu.data;

public class User {

  private String email;
  // private String aboutMe;
  private String[] userProfile;

  public User(String email, String[] userProfile) {
    this.email = email;
    // this.aboutMe = aboutMe;
    this.userProfile = userProfile;
  }

  public String getEmail() {
    return email;
  }

  /* public String getAboutMe() {
    return aboutMe;
  } */
  
  public String[] getProfile() {
    return userProfile;
  }
  
  public String toString() {
    return "{" + this.email + ", " + this.userProfile[0] + "}";
  }
}