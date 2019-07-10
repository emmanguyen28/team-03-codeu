package com.google.codeu.data;

public class User {

  private String email;
  private String aboutMe;
  private String name;
  public User(String email, String aboutMe) {
    this(email,aboutMe, null );
  }

  public User(String email, String aboutMe, String name) {
    this.email = email;
    this.aboutMe = aboutMe;
    this.name = name;
  }

  public String getName(){
      return name;
  }

  public String getEmail(){
    return email;
  }

  public String getAboutMe() {
    return aboutMe;
  }
}