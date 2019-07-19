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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
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
		messageEntity.setProperty("conversationTopicId", message.getConversationTopicId());

		datastore.put(messageEntity);
	}

	/** Stores conversation topic in Datastore. */
	public void storeConversationTopic(ConversationTopic conversationTopic) {
		System.out.println("storing conversation topic");
		Entity conversationTopicEntity = new Entity("ConversationTopic", conversationTopic.getId().toString());
		conversationTopicEntity.setProperty("title", conversationTopic.getTitle());
		conversationTopicEntity.setProperty("creator", conversationTopic.getCreator());
		conversationTopicEntity.setProperty("timestamp", conversationTopic.getTimestamp());

		datastore.put(conversationTopicEntity);
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
				String conversationTopicId = (String) entity.getProperty("conversationTopicId");
				System.out.println(conversationTopicId);

				Message message = new Message(id, user, text, timestamp, imageUrl, conversationTopicId);
				messages.add(message);
			} catch (Exception e) {
				System.err.println(String.format("Error reading message: [%s]", entity.toString()));
				e.printStackTrace();
			}
		}

		return messages;
	}

	/**
	 * Gets messages posted by a specific user. (excluding those in conversation
	 * topic chatrooms)
	 *
	 * @return a list of messages posted by the user, or empty list if user has
	 *         never posted a message. List is sorted by time descending.
	 */
	public List<Message> getMessages(String user) {

		Filter userFilter = new Query.FilterPredicate("user", FilterOperator.EQUAL, user);
		Filter conversationTopicFilter = new Query.FilterPredicate("conversationTopicId", FilterOperator.EQUAL, null);
		Filter messagesNotInConversationTopicFilter = CompositeFilterOperator.and(userFilter, conversationTopicFilter);

		Query query = new Query("Message").setFilter(messagesNotInConversationTopicFilter).addSort("timestamp",
				SortDirection.DESCENDING);

		return getQueryMessages(query);
	}

	/**
	 * Get all messages from all users (including messages in chatrooms)
	 */
	public List<Message> getAllMessages() {
		Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);

		return getQueryMessages(query);
	}

	/**
	 * Get all messages from all users (excluding messages in chatrooms)
	 */
	public List<Message> getAllFeedMessages() {
		Filter conversationTopicFilter = new Query.FilterPredicate("conversationTopicId", FilterOperator.EQUAL, null);
		Query query = new Query("Message").setFilter(conversationTopicFilter).addSort("timestamp",
				SortDirection.DESCENDING);
		return getQueryMessages(query);
	}

	/**
	 * Get the messages for the conversation topic matching the id passed as parameter
	 */
	public List<Message> getConversationTopicMessages(String id) {
		Filter conversationTopicFilter = new Query.FilterPredicate("conversationTopicId", FilterOperator.EQUAL, id);
		Query query = new Query("Message").setFilter(conversationTopicFilter).addSort("timestamp",
				SortDirection.ASCENDING);
		return getQueryMessages(query);
	}

	/**
	 * Get the conversation topic object matching the id passed as parameter
	 */
	public ConversationTopic getConversationTopic(String id) {
		Filter idFilter = new Query.FilterPredicate("id", FilterOperator.EQUAL, id);
		Query query = new Query("ConversationTopic").setFilter(idFilter);
		return getQueryConversationTopics(query).get(0);
	}

	/**
	 * Fetches and returns a list of all users
	 */
	public Set<String> getUsers() {
		Set<String> users = new HashSet<>();
		Query query = new Query("Message");
		PreparedQuery results = datastore.prepare(query);
		for (Entity entity : results.asIterable()) {
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
		for (Message message : allMessages) {
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

	/** Get all conversation topics */
	public List<ConversationTopic> getAllConversationTopics() {
		Query query = new Query("ConversationTopic").addSort("timestamp", SortDirection.DESCENDING);

		return getQueryConversationTopics(query);
	}

	/**
	 * Get conversation topics matching a specific query
	 */
	public List<ConversationTopic> getQueryConversationTopics(Query query) {

		List<ConversationTopic> conversationTopics = new ArrayList<>();
		PreparedQuery results = datastore.prepare(query);

		for (Entity entity : results.asIterable()) {
			try {
				String idString = entity.getKey().getName();
				UUID id = UUID.fromString(idString);
				String creator = (String) entity.getProperty("creator");
				String title = (String) entity.getProperty("title");
				long timestamp = (long) entity.getProperty("timestamp");

				ConversationTopic conversationTopic = new ConversationTopic(id, title, creator, timestamp);
				conversationTopics.add(conversationTopic);
			} catch (Exception e) {
				System.err.println(String.format("Error reading message: [%s]", entity.toString()));
				e.printStackTrace();
			}
		}

		return conversationTopics;
	}
}
