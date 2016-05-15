package com.cooksys.airline.models;

public class SeatsTaken 
{
	private Integer flightId;
	private Long numberOfTickets;
	
	public SeatsTaken(Integer flightId, Long numberOfTickets)
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

	public Long getNumberOfTickets()
	{
		return numberOfTickets;
	}

	public void setNumberOfTickets(Long numberOfTickets)
	{
		this.numberOfTickets = numberOfTickets;
	}
	
	
}
