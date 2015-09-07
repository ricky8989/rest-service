package com.ricky.rest.messenger.dao;

import java.util.HashMap;
import java.util.Map;

import com.ricky.rest.messenger.model.Comment;
import com.ricky.rest.messenger.model.Message;
import com.ricky.rest.messenger.model.Profile;

public class DatabaseClass {
	private static Map<Long, Message> messages = new HashMap<>();
	private static Map<String, Profile> profiles = new HashMap<>(); //using the name as the key for the Map
	
	static {
		Message message1 = new Message(1L,"Hello World!", "Ricky");
		Message message2 = new Message(2L,"Hello Jersey!", "Ricky");
		
		Comment comment1 = new Comment(1L,"Comment one","ricky");
		Comment comment2 = new Comment(2L,"Comment two","ricky");
		message1.getComments().put(1l, comment1);
		message1.getComments().put(2l, comment2);
		
		messages.put(1l, message1);
		messages.put(2l, message2);
		
		profiles.put("ricky", new Profile(1l,"ricky","Ricky","Balkissoon"));
	}
	
	public static Map<Long,Message> getMessages(){
		return messages;
	}

	public static Map<String,Profile> getProfiles(){
		return profiles;
	}

}