package com.agrahame.frabbit;

import io.vertx.core.json.JsonObject;

public class Frabbitter {

	private String id;
	private String emailAddress;
	private String givenName;
	private String familyName;

	public Frabbitter(String emailAddress, String givenName, String familyName) {
		this.id = "";
		this.emailAddress = emailAddress;
		this.givenName = givenName;
		this.familyName = familyName;
	}

	public Frabbitter(JsonObject json) {
		this.id = json.getString("_id");
		this.emailAddress = json.getString("email");
		this.givenName = json.getString("givenName");
		this.familyName = json.getString("familyName");
	}
	
	public Frabbitter() {
		this.id = "";
	}


	public JsonObject toJson() {
		JsonObject json = new JsonObject()
				.put("email", emailAddress)
				.put("givenName", givenName)
				.put("familyName", familyName);
	    if (id != null && !id.isEmpty()) {
	        json.put("_id", id);
	      }
		return json;
	}

	public String getFullName() {
		return getGivenName() + " " + getFamilyName();
	}

	public String getFamilyName() {
		return familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}
	
	public String getId() {
		return id;
	}

	public Frabbitter setGivenName(String name) {
		this.givenName = name;
		return this;
	}

	public Frabbitter setFamilyName(String name) {
		this.familyName = name;
		return this;
	}

	public Frabbitter setEmailAddress(String email) {
		this.emailAddress = email;
		return this;
	}

	public Frabbitter setId(String id) {
		this.id = id;
		return this;
	}

	@Override
	public String toString() {
		return "Frabbitter [id=" + id + ", emailAddress=" + emailAddress + ", givenName=" + givenName + ", familyName="
				+ familyName + "]";
	}
	
	

}
