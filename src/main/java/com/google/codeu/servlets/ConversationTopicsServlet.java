package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.ConversationTopic;
import com.google.codeu.data.Datastore;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Handles fetching and saving {@link CconversationTopic instances} instances.
 */
@WebServlet("/conversation-topics")
public class ConversationTopicsServlet extends HttpServlet {

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Responds with a JSON representation of all {@link ConversationTopic} objects
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("This will be a list of conversation topics in json format");
        response.setContentType("application/json");

        List<ConversationTopic> conversationTopics = datastore.getAllConversationTopics();
        Gson gson = new Gson();
        String json = gson.toJson(conversationTopics);
        
        response.getOutputStream().println(json);
    }

    /** Stores a new {@link ConversationTopic}. */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("This will create a new conversation topic instance and redirect you to that intance");
        
        UserService userService = UserServiceFactory.getUserService();
        String user = userService.getCurrentUser().getEmail();
        String conversationTopicTitle = Jsoup.clean(request.getParameter("conversationTopicTitle"), Whitelist.none());

        ConversationTopic conversationTopic = new ConversationTopic(conversationTopicTitle, user);
        System.out.println(conversationTopic.getCreator());
        System.out.println(conversationTopic.getId());
        datastore.storeConversationTopic(conversationTopic);
        // redirect to that conversation topic we just created
        response.sendRedirect("conversation-topics.html");
    }
}