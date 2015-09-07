package com.ricky.rest.messenger.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.ricky.rest.messenger.bean.MessageFilterBean;
import com.ricky.rest.messenger.exception.DataNotFoundException;
import com.ricky.rest.messenger.model.Message;
import com.ricky.rest.messenger.service.MessageService;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON) 
@Produces(value={MediaType.APPLICATION_JSON,MediaType.TEXT_XML})//set header 'Accept=application/json' or 'Accept=text/xml' to correct response
public class MessageResource {
	private MessageService service = new MessageService();
	
	/*
	@GET
	public List<Message> getAllMessages(@QueryParam("year") int year,
										@QueryParam("start") @DefaultValue("-1") int start,
										@QueryParam("size") @DefaultValue("-1") int size){
		//valid url:
		//get all messages  ../messages
		//get 2015 messages ../messages?year=2105
		//get 5 messages starting from 2 ../messages?start=2&size=5
		System.out.println();
		if(year > 0){
			return service.getAllMessagesForYear(year);
		}
		if(start >= 0 && size >= 0){
			return service.getAllMessagesPaginated(start, size);
		}
		return service.getAllMessages();
	}
	*/

	/**
	 * This method uses bean to get the query params as done above;
	 * It is better to use when you need to get a lot of query params
	 */
	@GET
	public List<Message> getAllMessages(@BeanParam MessageFilterBean filterBean){
		System.out.println();
		if(filterBean.getYear() > 0){
			return service.getAllMessagesForYear(filterBean.getYear());
		}
		if(filterBean.getStart() >= 0 && filterBean.getSize() >= 0){
			return service.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
		}
		return service.getAllMessages();
	}

	@GET
	@Path("/{messageId}")
	public Message getMessages(@PathParam("messageId") long id, @Context UriInfo uriInfo){ 
		//note: jersey automatically converted messageId which is a string to long
		Message message = service.getMessage(id);
		if(message == null){
			throw new DataNotFoundException("Message with id "+id+" is not found.");
		}
		createHateoasLinks(uriInfo, message);
		return message;
	}

	private void createHateoasLinks(UriInfo uriInfo, Message message) {
		message.getLinks().clear();
		message.addLink(getUriForSelf(uriInfo, message), "self");
		message.addLink(getUriForProfile(uriInfo, message), "profile");
		message.addLink(getUriForComments(uriInfo, message), "comments");
	}
	
	private String getUriForSelf(UriInfo uriInfo, Message message) {
		//adding HATEOAS self url
		String uri = uriInfo.getBaseUriBuilder() 	// --> http://localhost:8080/messenger/webapi/
			.path(MessageResource.class) 			// --> messages
			.path(Long.toString(message.getId())) 	// --> /{messageId}
			.build()
			.toString();
		return uri;
	}
	
	private String getUriForProfile(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder()
			.path(ProfileResource.class) 		
			.path(message.getAuthor()) 			
			.build();
		return uri.toString();
	}

	private String getUriForComments(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder()						// --> http://localhost:8080/messenger/webapi/
				.path(MessageResource.class)						// --> messages
				.path(MessageResource.class, "getCommentResource")	// --> {messageId}/comments
				.path(CommentResource.class)						// --> /
				.resolveTemplate("messageId", message.getId())		//replace {messageId} with value
				.build();
		return uri.toString();
	}

	@POST
	public Response addMessages(Message message, @Context UriInfo uriInfo){
		//note: jersey automatically populate Message object 
		//with the json being posted.
		Message newMessage = service.addMessage(message);
		String messageId = String.valueOf(message.getId()); //convert long to string
		
		//Using Response to set status code to status created (201),add the new Message; 
		//also adding the url location in the response header 'location' param for this message
		URI uri = uriInfo.getAbsolutePathBuilder().path(messageId).build(); //get uri path and add msg id
		return Response.created(uri) //set status code to 201 and location param
				.entity(newMessage)  //add the message as the content
				.build();
	}
	
	@PUT
	@Path("/{messageId}")
	public Message updateMessages(@PathParam("messageId") long id, Message message){
		//note: jersey automatically populate Message object 
		//with the json being posted.
		message.setId(id);
		return service.updateMessage(message);
	}
	
	@DELETE
	@Path("/{messageId}")
	public void deleteMessage(@PathParam("messageId") long id){
		service.removeMessage(id);
	}
	
	//Delegate all comments operations to CommentResource which is a 
	//sub-resource of MessageResource
	@Path("/{messageId}/comments")
	public CommentResource getCommentResource(){
		return new CommentResource();
	}
}
