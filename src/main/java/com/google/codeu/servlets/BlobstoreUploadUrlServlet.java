package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/* Returns URL of the image/file that Blobstore stored*/

@WebServlet("/blobstore-upload-url")
public class BlobstoreUploadUrlServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	    String uploadUrl = blobstoreService.createUploadUrl("/messages") ;

	    response.setContentType("text/html");
	    response.getOutputStream().println(uploadUrl);
	}
}