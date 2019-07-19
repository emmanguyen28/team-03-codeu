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

package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Handles fetching and saving {@link Message} instances. */
@WebServlet("/messages")
public class MessageServlet extends HttpServlet {

	private Datastore datastore;

	@Override
	public void init() {
		datastore = new Datastore();
	}

	/**
	 * Responds with a JSON representation of {@link Message} data for a specific
	 * user. Responds with an empty array if the user is not provided.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("application/json");
		// first, check if the user is provided
		String user = request.getParameter("user");

		if (user == null || user.equals("")) {
			// Request is invalid, return empty array
			response.getWriter().println("[]");
			return;// stop
		}
		// get a list of the user's messages using the Datastore API
		List<Message> messages = datastore.getMessages(user);
		Gson gson = new Gson();
		String json = gson.toJson(messages);

		response.getWriter().println(json);
	}

	/** Stores a new {@link Message}. */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		UserService userService = UserServiceFactory.getUserService();
		if (!userService.isUserLoggedIn()) {
			response.sendRedirect("/index.html");
			return;
		}
		
		String user = userService.getCurrentUser().getEmail();
		String text = Jsoup.clean(request.getParameter("text"), Whitelist.none());
		
		// Get the URL of the image the user uploaded on the Blobstore
		String imageUrls = getUploadedFileUrl(request, "image");

		String tag = Jsoup.clean(request.getParameter("tag"), Whitelist.none()); 
		System.out.println(imageUrls);
		
		// if imageUrls is null, it's saved like that. Will be taken care of on the front end
		

		String conversationTopicId = Jsoup.clean(request.getParameter("conversationTopicId"), Whitelist.none()); 

		Message message = new Message( user, text, imageUrls, tag, conversationTopicId);

		datastore.storeMessage(message);

		// if message has conversation topic id, then just reload page
		if (conversationTopicId != null) {
			response.sendRedirect("/single-conversation-topic.html?id=" + conversationTopicId);
		} else {
			response.sendRedirect("/user-page.html?user=" + user);
		}
	}

	/**
	 * Returns a URL that points to the uploaded file, or null if the user didn't
	 * upload a file.
	 */
	public String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
		List<BlobKey> blobKeys = blobs.get("image");
		System.out.println("blobKeys: " + blobKeys);

		// if our form doesn't contain a file input at all
		if (blobKeys == null)
			return null;

		// User submitted form without selecting a file, so we can't get a URL.
		// (DEVSERVER)
		if (blobKeys == null || blobKeys.isEmpty()) {
			return null;
		}

		// Our form only contains a single file input, so get the first index.
		BlobKey blobKey = blobKeys.get(0);

		// User submitted form without selecting a file, so we can't get a URL.
		// (LIVE SERVER)
		// BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
		// if (blobInfo.getSize() == 0) {
		// 	blobstoreService.delete(blobKey);
		// 	return null;
		// }

		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
		return imagesService.getServingUrl(options);
	}
}
