package com.cooksys.airline.models;

public class SeatsTaken 
{
	private Integer flightId;
	private Integer numberOfTickets;
	
	public SeatsTaken(Integer flightId, Integer numberOfTickets)
	{
		this.flightId = flightId;
		this.numberOfTickets = numberOfTickets;
	}

	public Integer getFlightId()
	{
		return flightId;
	}

	public void setFlightId(Integer flightId)
	{
		this.flightId = flightId;
	}

	public Integer getNumberOfTickets()
	{
		return numberOfTickets;
	}

	public void setNumberOfTickets(Integer numberOfTickets)
	{
		this.numberOfTickets = numberOfTickets;
	}
	
	
}
