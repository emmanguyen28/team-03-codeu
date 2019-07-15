package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;

@WebServlet("/single-conversation-topic")
public class SingleConversationTopicServlet extends HttpServlet {

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Responds with a JSON representation of the {@link Messages} data for
     * the specific chatroom
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("SingleConversationTopicServlet - getting messages for a specificconversation topic");

        response.setContentType("application/json");

        String conversationTopicId = request.getParameter("id");

        if (conversationTopicId == null || conversationTopicId.equals("")) {
			// Request is invalid, return empty array
			response.getWriter().println("[]");
			return;// stop
		}

        List<Message> messages = datastore.getConversationTopicMessages(conversationTopicId);
        Gson gson = new Gson();
		String json = gson.toJson(messages);

        response.getOutputStream().println(json);
    }
}