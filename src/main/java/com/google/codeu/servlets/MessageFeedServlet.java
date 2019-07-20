package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Handle fetching all messages for the public feed
 */
@WebServlet("/feed")
public class MessageFeedServlet extends HttpServlet {

    private Datastore datastore; 

    @Override
    public void init() {
        datastore = new Datastore(); 
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");


		// first, check if a tag is provided
        String tag = request.getParameter("tag");
        
        List<Message> messages = datastore.getAllMessages(); 


		// if (!tag.equals("null") && tag.length() > 0) {
        //     messages = datastore.getAllMessagesWithTag(tag.toLowerCase());
        // }
		// get a list of the user's messages using the Datastore API
		Gson gson = new Gson(); 
        String json = gson.toJson(messages); 
        response.getWriter().println(json);
    }

    /* Send entered tag to search */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String tag = Jsoup.clean(request.getParameter("tag"), Whitelist.none());        
		response.sendRedirect("/feed.html?tag=" + tag);
	}
}