// /* package com.google.codeu.servlets;

// import java.io.IOException;
// import java.util.List;

// import javax.servlet.annotation.WebServlet;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

// import com.google.codeu.data.Datastore;
// import com.google.codeu.data.Profile;
// import com.google.gson.Gson;


// /**
//  * Handle fetching all messages for the public feed
//  */
// @WebServlet("/updateName")
// public class NameServlet extends HttpServlet {

//     private Datastore datastore; 

//     @Override
//     public void init() {
//         datastore = new Datastore(); 
//     }

//     @Override
//     public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//         response.setContentType("application/json");

//         List<Message> messages = datastore.getAllMessages(); 
//         Gson gson = new Gson(); 
//         String json = gson.toJson(messages); 

//         response.getOutputStream().println(json);
// 	}
	
// }

// @WebServlet("/updateUsername")
// public class NameServlet extends HttpServlet {

//     private Datastore datastore; 

//     @Override
//     public void init() {
//         datastore = new Datastore(); 
//     }

//     @Override
//     public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//         response.setContentType("text/html");

//         List<Message> messages = datastore.getAllMessages(); 
//         Gson gson = new Gson(); 
//         String json = gson.toJson(messages); 

//         response.getOutputStream().println(json);
// 	}
	
// 	/** Stores a new {@link Message}. */
// 	@Override
// 	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

// 		UserService userService = UserServiceFactory.getUserService();
// 		if (!userService.isUserLoggedIn()) {
// 			response.sendRedirect("/index.html");
// 			return;
// 		}
//     	String user = userService.getCurrentUser().getEmail();
// 		String text = Jsoup.clean(request.getParameter("text"), Whitelist.none());
// 		// Get the URL of the image the user uploaded on the Blobstore
// 		String imageUrls = getUploadedFileUrl(request, "image");
// 		System.out.println(imageUrls);
		
// 		// if imageUrls is null, it's saved like that. Will be taken care of on the front end
// 		Message message = new Message(user, text, imageUrls);
// 		datastore.storeMessage(message);

// 		response.sendRedirect("/user-page.html?user=" + user);
// 	}
// }

// @WebServlet("/updateInterests")
// public class NameServlet extends HttpServlet {

//     private Datastore datastore; 

//     @Override
//     public void init() {
//         datastore = new Datastore(); 
//     }

//     @Override
//     public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//         response.setContentType("application/json");

//         List<Message> messages = datastore.getAllMessages(); 
//         Gson gson = new Gson(); 
//         String json = gson.toJson(messages); 

//         response.getOutputStream().println(json);
//     }
// }
//  */