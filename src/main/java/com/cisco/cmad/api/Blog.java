package com.cisco.cmad.api;

import java.util.List;

public abstract interface Blog {
	public abstract String addUser(String userDetailsJson) throws BlogException;

	public abstract String updateUser(String userDetailsJson) throws BlogException;

	public abstract String getUserInfo(String email) throws BlogException;

	public abstract String addPost(String postJson) throws BlogException;

	public abstract String addComment(String commentJson, int BlogId) throws BlogException;

	public abstract List<String> getPosts() throws BlogException;

	public abstract List<String> getPosts(String searchString) throws BlogException;

	public abstract String getPost(int postId) throws PostNotFoundException, BlogException;

	public abstract String signIn(String authDataJson);

	public abstract int getPostsCount() throws BlogException;
}