package com.cisco.cmad.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.cisco.cmad.api.Blog;
import com.cisco.cmad.biz.SimpleBlog;

@Path("/blog")
public class BlogController {
	public BlogController() {
		System.out.println("BlogController.BlogController()");
	}

	@POST
	@Consumes({ "application/json" })
	@Path("/registerUser")
	public Response createUserInfo(String userInfoJson) {
//		System.out.println("BlogController.createUserInfo() : userInfoJson"+userInfoJson);
		Blog blog = new SimpleBlog();
		String email = blog.addUser(userInfoJson);
//		System.out.println("BlogController.createUserInfo() : email : " + email);
		if (email == null) {
			return Response.serverError().entity("Account already exists").build();
		}
		return Response.ok().entity(email).build();
	}

	@GET
	@Produces({ "application/json" })
	@Path("/userInfo/{email}")
	public Response getUserInfo(@PathParam("email") String email) {
		Blog blog = new SimpleBlog();
//		System.out.println("BlogController.getUserInfo()"+email);
		String userInfoJson = blog.getUserInfo(email);
		if (userInfoJson == null) {
			return Response.serverError().entity("Nothing found").build();
		}
		return Response.ok().entity(userInfoJson).build();
	}

	@GET
	@Produces({ "application/json" })
	@Path("/allPosts")
	public Response getPosts() {
		Blog blog = new SimpleBlog();
		List posts = blog.getPosts();
//		System.out.println("BlogController.getPosts() : post size : "+posts.size());
		if (posts == null) {
			return Response.serverError().entity("Nothing found").build();
		}
		return Response.ok().entity(posts).build();
	}

	@GET
	@Produces({ "application/json" })
	@Path("/allPostsCount")
	public Response getPostsCount() {
		Blog blog = new SimpleBlog();
		int postsCount = blog.getPostsCount();
		String json = "{\"postsCount\" :"+ postsCount+" }";
		return Response.ok().entity(json).build();
	}

	
	@GET
	@Produces({ "application/json" })
	@Path("/search")
	public Response getPosts(@QueryParam("searchString") String searchString) {
		Blog blog = new SimpleBlog();
//		System.out.println("BlogController.getPosts() : searchString : "+searchString);
		List posts = blog.getPosts(searchString);
//		System.out.println("BlogController.getPosts(searchString) : post size : "+posts.size());
		if (posts == null) {
			return Response.serverError().entity("Nothing found").build();
		}
		return Response.ok().entity(posts).build();
	}
	
	@GET
	@Produces({ "application/json" })
	@Path("/post/{postId}")
	public Response getPost(@PathParam("postId") String postId) {
		Blog blog = new SimpleBlog();
		System.out.println("BlogController.getPost() : postId : "+postId);
		String postJson = blog.getPost(Integer.parseInt(postId));
		System.out.println("BlogController.getPost() : postJson : "+postJson);
		if (postJson == null) {
			return Response.serverError().entity("Nothing found").build();
		}
		return Response.ok().entity(postJson).build();
	}
	
	@PUT
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@Path("/updateUserInfo")
	public Response updateUserInfo(String userInfoJson) {
		Blog blog = new SimpleBlog();
//		System.out.println("BlogController.updateUserInfo() : userInfoJson : "+userInfoJson);
		String username = blog.updateUser(userInfoJson);
//		System.out.println("BlogController.updateUserInfo() : username : "+username);
		if (username == null) {
			return Response.serverError().entity("Account updation failed").build();
		}
		return Response.ok().entity(username).build();
	}

	@POST
	@Consumes({ "application/json" })
	@Path("/addPost")
	public Response addPost(String postJson) {
		Blog blog = new SimpleBlog();
		String writtenToDbJson = blog.addPost(postJson);
//		System.out.println("BlogController.addPost() : writtenToDbJson : " + writtenToDbJson);
		if (writtenToDbJson == null) {
			return Response.serverError().entity("No such user").build();
		}
		return Response.ok().entity(writtenToDbJson).build();
	}
	
	@POST
	@Consumes({ "application/json" })
	@Path("/addComment")
	public Response addComment(String commentJson, @QueryParam("blogID") String blogId) {
		Blog blog = new SimpleBlog();
		String writtenToDbJson = blog.addComment(commentJson, Integer.parseInt(blogId));
//		System.out.println("BlogController.addComment() : writtenToDbJson : " + writtenToDbJson);
		if (writtenToDbJson == null) {
			return Response.serverError().entity("No such user").build();
		}
		return Response.ok().entity(writtenToDbJson).build();
	}

	@POST
	@Consumes({ "application/json" })
	@Path("/signIn")
	public Response signIn(String authDataJson) {
//		System.out.println("BlogController.signIn() : authDataJson : "+authDataJson);
		Blog blog = new SimpleBlog();
		String emailMatchFound = blog.signIn(authDataJson);
//		System.out.println("BlogController.signIn() : result : " + emailMatchFound);
		if (emailMatchFound == null) {
			return Response.serverError().entity("No such user").build();
		}
		return Response.ok().entity(emailMatchFound).build();
	}
}