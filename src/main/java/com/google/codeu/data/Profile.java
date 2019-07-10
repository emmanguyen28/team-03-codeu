package com.google.codeu.data;


import java.util.UUID;

public class Profile {

    private String name; 
    private String username;
    private String profile_pic;
    private UUID id;
    private String [] interests; 

    public Profile(String name, String username, String profile_pic_URL, String [] interests) {
        this(UUID.randomUUID(), name, username, profile_pic_URL,interests); 

    }

	public Profile(UUID id, String name, String username, String profile_pic_URL, String [] interests) {
        this.id = id;
        this.name = name; 
        this.username = username;
        this.profile_pic = profile_pic_URL;
        this.interests = interests;
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

    public String [] getInterests(){
        return this.interests;
    }

}
