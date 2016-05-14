package com.cooksys.core.models;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


public class FlightModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long secondsTillNextDay;
	private Integer currentDay;
	private List<Flight> flights;
	
	public Long getSecondsTillNextDay() {
		return secondsTillNextDay;
	}
	public void setSecondsTillNextDay(Long secondsTillNextDay) {
		this.secondsTillNextDay = secondsTillNextDay;
	}
	public Integer getCurrentDay() {
		return currentDay;
	}
	public void setCurrentDay(Integer currentDay) {
		this.currentDay = currentDay;
	}
	public List<Flight> getFlights() {
		return flights;
	}
	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
}
