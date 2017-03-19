package com.cisco.cmad.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidDataException extends BlogException implements ExceptionMapper<InvalidDataException> {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidDataException() {
		super("Invalid Credentials");
	}

	public InvalidDataException(String message) {
		super(message);
	}

	public InvalidDataException(Throwable cause) {
		super(cause);
	}

	public InvalidDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public Response toResponse(InvalidDataException exception) {
		// TODO Auto-generated method stub
		return Response.status(404).entity(exception.getMessage())
                .type("text/plain").build();
	}
}