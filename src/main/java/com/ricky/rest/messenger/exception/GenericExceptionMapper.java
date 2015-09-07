package com.ricky.rest.messenger.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import com.ricky.rest.messenger.model.ErrorMessage;

//@Provider
//commented out to demo pre-build jersey exception in the CommentResource file
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
	@Override
	public Response toResponse(Throwable exception) {
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setErrorCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		errorMessage.setErrorMessage(exception.getMessage());
		errorMessage.setDocumentation("http://<url-with-more-details>");
		
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
	}
}
