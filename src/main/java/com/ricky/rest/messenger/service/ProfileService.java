package com.ricky.rest.messenger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ricky.rest.messenger.dao.DatabaseClass;
import com.ricky.rest.messenger.model.Profile;

public class ProfileService {
	private Map<String, Profile> profiles = DatabaseClass.getProfiles();
	
	public ProfileService(){
		System.out.println("Intantiated " + System.currentTimeMillis());
	}
	
	public List<Profile> getAllProfiles(){
		return new ArrayList<Profile>(profiles.values());
	}
	
	public Profile getProfile(String profileName){
		return profiles.get(profileName);
	}
	
	public Profile updateProfile(Profile profile){
		if (profile != null && profile.getProfileName().isEmpty()){
			return null;
		}
		profiles.put(profile.getProfileName(), profile);
		return profile;
	}
	
	public Profile addProfile(Profile profile){
		profile.setId(profiles.size() + 1);
		profiles.put(profile.getProfileName(), profile);
		return profile;
	}

	public Profile removeProfile(String profileName){
		return profiles.remove(profileName);
	}
}
