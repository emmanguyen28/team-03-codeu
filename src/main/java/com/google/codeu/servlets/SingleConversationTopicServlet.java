package com.google.codeu.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.google.codeu.data.Datastore;

@WebServlet("/single-conversation-topic")
public class SingleConversationTopicServlet extends HttpServlet {

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /** Responds with a JSON representation of the {@link ConversationTopic} data for the specific chatroom */

}