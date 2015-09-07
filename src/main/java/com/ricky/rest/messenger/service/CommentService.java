package com.ricky.rest.messenger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ricky.rest.messenger.dao.DatabaseClass;
import com.ricky.rest.messenger.model.Comment;
import com.ricky.rest.messenger.model.ErrorMessage;
import com.ricky.rest.messenger.model.Message;


public class CommentService {
	//reference to the DAO
	private Map<Long,Message> messages = DatabaseClass.getMessages();

	
	/*
	 * Get all comments for a message
	 */
	public List<Comment> getAllComments(long messageId){
		Map<Long,Comment> comments = messages.get(messageId).getComments();
		if(comments == null) return null;
		return  new ArrayList<Comment>(comments.values());
	}
		
	/*
	 * Get a specific comment for a given message
	 */
	public Comment getComment(long messageId, long commentId){
		//Map<Long,Comment> comments = messages.get(messageId).getComments();
		//Demo usage of pre-defined exception
		
		//Using jersey exception is not recommend by some because
		//you are placing presentation code in the business layer

		Message message = messages.get(messageId);
		if(message == null){
			ErrorMessage errorMessage = new ErrorMessage(404,"Message Not Found","http://<url-with-more-details>");
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new WebApplicationException(response);
		}
		Map<Long,Comment> comments = message.getComments();
		Comment comment = comments.get(commentId);
		if (comment == null){
			ErrorMessage errorMessage = new ErrorMessage(404,"Comment Not Found","http://<url-with-more-details>");
			Response response = Response.status(Status.NOT_FOUND).entity(errorMessage).build();
			throw new NotFoundException(response);//you don't need to add the status code in Response
		}
		return comment;
	}
	
	/*
	 * Add a comment for a specific message
	 */
	public Comment addComment(long messageId, Comment comment){
		Map<Long,Comment> comments = messages.get(messageId).getComments();
		comment.setId(comments.size()+1);
		comments.put(comment.getId(), comment); 
		return comments.get(comment.getId());//get the comment added;
	}

	/*
	 * Update a specific comment on a message
	 */
	public Comment updateComment(long messageId, Comment comment){
		if(comment.getId() <= 0){
			return null;
		}
		
		Map<Long,Comment> comments = messages.get(messageId).getComments();
		comments.put(comment.getId(), comment);
		return comments.get(comment.getId());//retrieve the message just added
	}

	/*
	 * Delete a specific comment for a given message
	 */
	public Comment deleteComment(long messageId, long commentId){
		Map<Long,Comment> comments = messages.get(messageId).getComments();
		return comments.remove(commentId);
	}


}
