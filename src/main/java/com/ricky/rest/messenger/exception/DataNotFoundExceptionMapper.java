package com.ricky.rest.messenger.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ricky.rest.messenger.model.ErrorMessage;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

	@Override
	public Response toResponse(DataNotFoundException exception) {
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setErrorCode(Status.NOT_FOUND.getStatusCode());
		errorMessage.setErrorMessage(exception.getMessage());
		errorMessage.setDocumentation("http://<url-with-more-details>");
		
		return Response.status(Status.NOT_FOUND).entity(errorMessage).build();
	}

}
