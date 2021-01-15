package io.vertx.blog.first;

import java.util.concurrent.atomic.AtomicInteger;

public class Team {
	 private static final AtomicInteger COUNTER = new AtomicInteger();

	  private final int id;

	  private String name;

	  private String country;

	  public Team(String name, String country) {
	    this.id = COUNTER.getAndIncrement();
	    this.name = name;
	    this.country = country;
	  }

	  public Team() {
	    this.id = COUNTER.getAndIncrement();
	  }

	  public String getName() {
	    return name;
	  }

	  public String getCountry() {
	    return country;
	  }

	  public int getId() {
	    return id;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

	  public void setCountry(String country) {
	    this.country = country;
	  }
}
