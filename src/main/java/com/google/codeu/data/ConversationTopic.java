package com.google.codeu.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ConversationTopic {

	private UUID uuid;
	private String title;
	// maybe list of hashtags?
	private String creator;
	private long timeStamp;
	private Set<String> users;
	private List<Message> messages;
	// time it was created and by which user

	public ConversationTopic(String title, String creator) {
		this(UUID.randomUUID(), title, creator, System.currentTimeMillis(), new HashSet<String>(),
				new ArrayList<Message>());
	}

	public ConversationTopic(UUID uuid, String title, String creator, long timeStamp, Set<String> users,
			List<Message> messages) {
		this.uuid = uuid;
		this.title = title;
		this.creator = creator;
		this.timeStamp = timeStamp;
		this.users = users;
		this.messages = messages;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getTitle() {
		return title;
	}

	public String getCreator() {
		return creator;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public Set<String> getUsers() {
		return users;
	}

	public List<Message> getMessages() {
		return messages;
	}
}
