package com.cooksys.airline.models;

import java.util.List;

import com.cooksys.core.models.Flight;

public class Notification 
{
	private String status;
	private List<Trip> trips;
	private Flight flight;
	
	public Notification(String status, List<Trip> trips)
	{
		this.status = status;
		this.trips = trips;
	}
	
	public Notification(String status, Flight flight, List<Trip> trips)
	{
		this.status = status;
		this.flight = flight;
		this.trips = trips;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public List<Trip> getTrips()
	{
		return trips;
	}

	public void setTrips(List<Trip> trips)
	{
		this.trips = trips;
	}

	public Flight getFlight()
	{
		return flight;
	}

	public void setFlight(Flight flight)
	{
		this.flight = flight;
	}
	
	

}
