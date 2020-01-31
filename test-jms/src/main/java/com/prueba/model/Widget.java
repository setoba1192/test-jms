package com.prueba.model;

import java.io.Serializable;
import java.util.Date;

public class Widget implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private long time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {

		Date date = new Date(getTime());

		return "ID: " + getId() + " Timestamp: " + date;
	}

}
