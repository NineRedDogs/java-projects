package com.agrahame.frabbit;

import io.vertx.core.json.JsonObject;

public class Frabbitter {

	  private String id;

	  private String name;

	  private String emailAddress;

	  public Frabbitter(String name, String country) {
	    this.name = name;
	    this.emailAddress = country;
	    this.id = "";
	  }

	  public Frabbitter(JsonObject json) {
	    this.name = json.getString("name");
	    this.emailAddress = json.getString("email");
	    this.id = json.getString("_id");
	  }

	  public Frabbitter() {
	    this.id = "";
	  }

	  public Frabbitter(String id, String name, String country) {
	    this.id = id;
	    this.name = name;
	    this.emailAddress = country;
	  }

	  public JsonObject toJson() {
	    JsonObject json = new JsonObject()
	        .put("name", name)
	        .put("email", emailAddress);
	    if (id != null && !id.isEmpty()) {
	      json.put("_id", id);
	    }
	    return json;
	  }

	  public String getName() {
	    return name;
	  }

	  public String getEmailAddress() {
	    return emailAddress;
	  }

	  public String getId() {
	    return id;
	  }

	  public Frabbitter setName(String name) {
	    this.name = name;
	    return this;
	  }

	  public Frabbitter setCountry(String country) {
	    this.emailAddress = country;
	    return this;
	  }

	  public Frabbitter setId(String id) {
	    this.id = id;
	    return this;
	  }	
	
}
