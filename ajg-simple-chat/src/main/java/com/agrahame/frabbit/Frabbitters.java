package com.agrahame.frabbit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vertx.core.json.JsonObject;

public class Frabbitters {

	private final Map<String, Frabbitter> frabitters;

	/** lock object. */
	private static final Object LOCK = new Object();


	public Frabbitters() {
		frabitters = new HashMap<String, Frabbitter>();
	}

	public void initialise(List<JsonObject> frabs) {
		synchronized (LOCK) {
			for (JsonObject json : frabs) {
				Frabbitter f = new Frabbitter(json);
				frabitters.put(f.getId(), f);
			}
			showAllFrabs();
		}
	}

	private void showAllFrabs() {
		synchronized (LOCK) {
			for (Map.Entry<String, Frabbitter> entry : frabitters.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			}
		}
	}

	public Frabbitter get(final String id) {
		synchronized (LOCK) {
			return frabitters.get(id);
		}
	}

	public Frabbitter put(final Frabbitter frab) {
		synchronized (LOCK) {
			if (frab.getId() == null) {
				throw new IllegalArgumentException("Cannot put new frabbitter without valid ID ");
			}
			return frabitters.put(frab.getId(), frab);
		}
	}
	
	
	public boolean exists(final String id) {
		synchronized (LOCK) {
			return (frabitters.get(id) != null);
		}
	}

	public boolean emailExists(String emailAddress) {
		synchronized (LOCK) {
			for (Map.Entry<String, Frabbitter> entry : frabitters.entrySet()) {
				if (entry.getValue().getEmailAddress().equalsIgnoreCase(emailAddress)) {
					// this email address already exists
					return true;
				}
			}
			// no matches for this email address
			return false;
		}
	}

	public String getIdFromEmail(String emailAddress) {
		synchronized (LOCK) {
			for (Map.Entry<String, Frabbitter> entry : frabitters.entrySet()) {
				if (entry.getValue().getEmailAddress().equalsIgnoreCase(emailAddress)) {
					// this email address already exists, return its ID
					return entry.getKey();
				}
			}
			// no matches for this email address
			return null;
		}
	}






}
