package com.google.codeu.data;

import java.util.UUID;

/** A single message posted by a user. */
public class Message {

  private UUID id;
  private String user;
  private String text;
  private long timestamp;
  private String imageUrl;
  private String tag; 
  private String conversationTopicId;

  /**
   * Constructs a new {@link Message} posted by {@code user} with {@code text} content. Generates a
   * random ID and uses the current system time for the creation time.
   */
  public Message(String user, String text) {
    this(UUID.randomUUID(), user, text, System.currentTimeMillis(), null, "", null); // imageUrl is null if user didn't upload an image
  }
  
  public Message(String user, String text, String imageUrl) {
	  this(UUID.randomUUID(), user, text, System.currentTimeMillis(), imageUrl, "", null);
  }

  public Message(String user, String text, String imageUrl, String tag, String conversationTopicId) {
    this(UUID.randomUUID(), user, text, System.currentTimeMillis(), imageUrl, tag, conversationTopicId);
  }

  public Message(UUID id, String user, String text, long timestamp, String imageUrl, String tag, String conversationTopicId) {
    this.id = id;
    this.user = user;
    this.text = text;
    this.timestamp = timestamp;
    this.imageUrl = imageUrl;
    this.tag = tag; 
    this.conversationTopicId = conversationTopicId;
  }

  public UUID getId() {
    return id;
  }

  public String getUser() {
    return user;
  }

  public String getText() {
    return text;
  }

  public long getTimestamp() {
    return timestamp;
  }
  
  public String getImageUrl() {
	  return imageUrl;
  }

  public String getTag() {
    return tag; 
  }

  public String getConversationTopicId() {
    return conversationTopicId;
  }
}