package com.cisco.cmad.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PostNotFoundException extends BlogException implements ExceptionMapper<PostNotFoundException> {
	
	private static final long serialVersionUID = 2L;
	
	public PostNotFoundException() {
		super("Post Not Found");
	}

	public PostNotFoundException(String message) {
		super(message);
	}

	public PostNotFoundException(Throwable cause) {
		super(cause);
	}

	public PostNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PostNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public Response toResponse(PostNotFoundException exception) {
		// TODO Auto-generated method stub
		return Response.status(404).entity(exception.getMessage())
                .type("text/plain").build();
	}
}