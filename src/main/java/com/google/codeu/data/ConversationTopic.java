package com.google.codeu.data;

import java.util.UUID;

public class ConversationTopic {

	private UUID id;
	private String title;
	private String creator;
	private long timestamp;

	public ConversationTopic(String title, String creator) {
		this(UUID.randomUUID(), title, creator, System.currentTimeMillis());
	}

	public ConversationTopic(UUID id, String title, String creator, long timestamp) {
		this.id = id;
		this.title = title;
		this.creator = creator;
		this.timestamp = timestamp;
	}

	public UUID getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getCreator() {
		return creator;
	}

	public long gettimestamp() {
		return timestamp;
	}
}
