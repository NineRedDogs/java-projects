package io.vertx.blog.first;

import io.vertx.core.json.JsonObject;

public class Team {

  private String id;

  private String name;

  private String country;

  public Team(String name, String country) {
    this.name = name;
    this.country = country;
    this.id = "";
  }

  public Team(JsonObject json) {
    this.name = json.getString("name");
    this.country = json.getString("country");
    this.id = json.getString("_id");
  }

  public Team() {
    this.id = "";
  }

  public Team(String id, String name, String country) {
    this.id = id;
    this.name = name;
    this.country = country;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject()
        .put("name", name)
        .put("country", country);
    if (id != null && !id.isEmpty()) {
      json.put("_id", id);
    }
    return json;
  }

  public String getName() {
    return name;
  }

  public String getCountry() {
    return country;
  }

  public String getId() {
    return id;
  }

  public Team setName(String name) {
    this.name = name;
    return this;
  }

  public Team setCountry(String country) {
    this.country = country;
    return this;
  }

  public Team setId(String id) {
    this.id = id;
    return this;
  }
}