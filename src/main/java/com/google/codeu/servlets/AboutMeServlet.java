package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;
import com.google.codeu.data.Profile;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import com.google.gson.Gson;

// import static com.googlecode.objectify.ObjectifyService.ofy;


/**
 * Handles fetching and saving user data.
 */
@WebServlet("/about")
public class AboutMeServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with the "about me" section for a particular user.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

        response.setContentType("application/json");

        String user_email = request.getParameter("user");


        
        if ( user_email == null || user_email.equals("") ) {
          // Request is invalid, return empty array
          
          response.getWriter().println("[]");
          return;// stop
        }


        Profile userData = datastore.getUser(user_email);

        

        Gson gson = new Gson();
		    String json = gson.toJson(userData);

        response.getOutputStream().println(json);

        // String name = "FakeName"; 
        // String username = "FakeUsername";
        // String profile_pic = "Fake";
        // String[] interests = {"muscles","cardio"}; 

        // Profile newProfile = new Profile(name, username, profile_pic, interests);
        // // datastore.storeUser(newProfile);

        // response.getOutputStream().println(newProfile.getName());
        // for (int i = 0; i < newProfile.getInterests().length; i++) {
        //   response.getOutputStream().println(newProfile.getInterests()[i]);

        }
  

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    UserService userService = UserServiceFactory.getUserService();

    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String userEmail = userService.getCurrentUser().getEmail();
    String name = "";
    String username = "";
    String interests = "";



    if (datastore.getUser(userEmail) != null){
      Profile newProfile = datastore.getUser(userEmail);
      name = newProfile.getName();
      username = newProfile.getUsername(); 
      interests = newProfile.getInterests();

    } 

    // String name =  Jsoup.clean(request.getParameter("user-name"), Whitelist.none()) ; 

    if (request.getParameter("user-name") != null){
      name =  Jsoup.clean(request.getParameter("user-name"), Whitelist.none()) ; 

    } 

    if(request.getParameter("user-username") != null){
      username =  Jsoup.clean(request.getParameter("user-username"), Whitelist.none()) ; 

    }

    if(request.getParameter("user-interests") != null){
      interests =  Jsoup.clean(request.getParameter("user-interests"), Whitelist.none()) ; 

    }

    // String name =  Jsoup.clean(request.getParameter("about-me"), Whitelist.none()) ;   
    // String name =  Jsoup.clean(request.getParameter("about-me"), Whitelist.none()) ;   

    Profile newProfile = new Profile(name, username, "", interests, userEmail);
    // String name, String username, String profile_pic_URL, String interests, String email)


    
    datastore.storeUser(newProfile);

    response.sendRedirect("/user-page.html?user=" + userEmail);
  }
}