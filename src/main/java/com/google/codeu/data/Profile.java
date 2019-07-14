package com.google.codeu.data;


import java.util.UUID;

// import com.googlecode.objectify.annotation.Entity;
// import com.googlecode.objectify.annotation.Id;
// import com.googlecode.objectify.annotation.Index;

public class Profile {

    private String name; 
    private String username;
    private String profile_pic;
    private UUID id;
    private String interests; 
    private String email;

    public Profile(String name, String username, String profile_pic_URL, String interests, String email) {
        this(UUID.randomUUID(), name, username, profile_pic_URL,interests, email); 
    }

    public Profile(String name, String email){
        this(UUID.randomUUID(), name, '', '','', email); 
       
    }

    public Profile(String name, String username, String email){
        this(UUID.randomUUID(), name, username, '','', email);         
    }


	public Profile(UUID id, String name, String username, String profile_pic_URL, String interests, String email) {
        this.id = id;
        this.name = name; 
        this.username = username;
        this.profile_pic = profile_pic_URL;
        this.interests = interests;
        this.email = email;
    }

    public String getEmail(){
        return this.email;
      }    
    
    public UUID getID(){
        return this.id;
    }

	public String getName(){
        return this.name;
    }
    public String getUsername(){
        return this.username;
    }

    public String getProfilePic(){
        return this.profile_pic;
    }

    public String getInterests(){
        return this.interests;
    }

}
