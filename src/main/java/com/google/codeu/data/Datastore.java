/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.*; 

/** Provides access to the data stored in Datastore. */
public class Datastore {

  
	private DatastoreService datastore;

	public Datastore() {
		datastore = DatastoreServiceFactory.getDatastoreService();
	}

	/** Stores the Message in Datastore. */
	public void storeMessage(Message message) {
		Entity messageEntity = new Entity("Message", message.getId().toString());
		messageEntity.setProperty("user", message.getUser());
		messageEntity.setProperty("text", message.getText());
		messageEntity.setProperty("timestamp", message.getTimestamp());
		messageEntity.setProperty("imageUrl", message.getImageUrl());

		datastore.put(messageEntity);
	}

	/** Stores the User in Datastore. */
	public void storeUser(Profile user) {
		Entity userEntity = new Entity("Profile", user.getEmail());
		userEntity.setProperty("name", user.getName());
		userEntity.setProperty("username", user.getUsername());
		userEntity.setProperty("profile_pic", user.getProfilePic());
		userEntity.setProperty("interests", user.getInterests());
		userEntity.setProperty("email", user.getEmail());
		
		System.out.println(userEntity.toString());
		System.out.println(userEntity.getKey());

		System.out.println("Leaving datastore...");
		datastore.put(userEntity);
	}

	
	   
	   /**
		* Returns the User owned by the email address, or
		* null if no matching User was found.
		*/
	   public Profile getUser(String email) {
	   
		Query query = new Query("Profile")
		  .setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));
		PreparedQuery results = datastore.prepare(query);
		Entity userEntity = results.asSingleEntity();
		if(userEntity == null) {
		 return null;
		}
		
		// String aboutMe = (String) userEntity.getProperty("aboutMe");

		String name = (String) userEntity.getProperty("name");
		String username = (String) userEntity.getProperty("username");
		String profile_pic = (String) userEntity.getProperty("profile_pic");
		String interests = (String) userEntity.getProperty("interests");

		Profile user = new Profile(name, username, profile_pic, interests, email) ;

		return user;
	   }

	/**
	 * Get messages from a certain query
	 * 
	 * @param query
	 * @return a list of messages from this query
	 */
	public List<Message> getQueryMessages(Query query) {

		List<Message> messages = new ArrayList<>();

		PreparedQuery results = datastore.prepare(query);

		for (Entity entity : results.asIterable()) {

			try {
				String idString = entity.getKey().getName();
				UUID id = UUID.fromString(idString);
				String user = (String) entity.getProperty("user");
				String text = (String) entity.getProperty("text");
				long timestamp = (long) entity.getProperty("timestamp");
				String imageUrl = (String) entity.getProperty("imageUrl");

				Message message = new Message(id, user, text, timestamp, imageUrl);
				messages.add(message);
			} catch (Exception e) {
				//System.err.println("Error reading message.");
        //System.err.println(entity.toString());
        System.err.println(String.format("Error reading message: [%s]", entity.toString()));
				e.printStackTrace();
			}
		}

		return messages;
	}

	/**
	 * Gets messages posted by a specific user.
	 *
	 * @return a list of messages posted by the user, or empty list if user has
	 *         never posted a message. List is sorted by time descending.
	 */
	public List<Message> getMessages(String user) {
		Query query = new Query("Message").setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user))
				.addSort("timestamp", SortDirection.DESCENDING);

		return getQueryMessages(query);
	}

	/**
	 * Get all messages from all users
	 */
	public List<Message> getAllMessages() {
		Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);

		return getQueryMessages(query);

	}
	/**
	 * Fetches and returns a list of all users 
	 */
	public Set<String> getUsers(){
		  Set<String> users = new HashSet<>();
		  Query query = new Query("Message");
		  PreparedQuery results = datastore.prepare(query);
		  for(Entity entity : results.asIterable()) {
		    users.add((String) entity.getProperty("user"));
		  }
		  return users;
	}

	/**
	 * Returns the total number of messages for all users.
	 */
	public int getTotalMessageCount() {
		Query query = new Query("Message");
		PreparedQuery results = datastore.prepare(query);
		return results.countEntities(FetchOptions.Builder.withLimit(1000));
	}
	
	/**
	 * Returns the total length of all messages
	 */
	public int getTotalMessageLength() {
		int totalMsgLength = 0;
		List<Message> allMessages = getAllMessages();
		for(Message message : allMessages) {
			totalMsgLength += message.getText().length();
		}
		return totalMsgLength;
	}
	
	/**
	 * Returns the average length of messages
	 */
	public int getAverageMessageLength() {
		return getTotalMessageLength() / getAllMessages().size();
	}
}
